import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ReferenceLine,
  ResponsiveContainer,
} from 'recharts';

import { useState, useMemo, useEffect } from 'react';

import { validateData } from '../Validation';

export const CurvaPermanencia = ({ dados, codigoEstacao }: { dados: any[], codigoEstacao: any }) => {
  const [tipoCurva, setTipoCurva] = useState<'empirica' | 'logaritmica'>('empirica');
  const [qPersonalizado, setQPersonalizado] = useState<string>(''); // agora como string
  const [vazaoPersonalizada, setVazaoPersonalizada] = useState<number | null>(null);
  const [origemDados, setOrigemDados] = useState<'mensal' | 'diaria'>('mensal'); // select de origem
  const [dadosVazao, setDadosVazao] = useState<any[]>(Array.isArray(dados) ? dados : []);
  const [vazoesDiariasCarregadas, setVazoesDiariasCarregadas] = useState<any[] | null>(null); // cache

    const [dataMinima, dataMaxima] = useMemo(() => {
    if (!dados.length) return [null, null];

    const ordenadas = [...dados].sort(
      (a, b) =>
        new Date(a.dataInicial).getTime() - new Date(b.dataInicial).getTime()
    );

    return [
      ordenadas[0].dataInicial.split('T')[0],
      ordenadas[ordenadas.length - 1].dataInicial.split('T')[0],
    ];
  }, [dados]);

  // Atualizar dados somente quando o usuário pedir por vazão diária
  useEffect(() => {
    if (origemDados === 'diaria') {
      if (vazoesDiariasCarregadas) {
        setDadosVazao(vazoesDiariasCarregadas); // já carregado anteriormente
      } else {
        if (codigoEstacao) {
          fetch('http://localhost:8080/api/vazoesDiarias', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({ codigoEstacao, dataInicio: dataMinima, dataFim: dataMaxima }),
          })
            .then((res) => res.json())
            .then((data) => {
              setVazoesDiariasCarregadas(data); // salva para não repetir
              setDadosVazao(data);
               {console.log(data.length)}
            })
            .catch((err) => {
              console.error('Erro ao buscar vazões diárias:', err);
              setDadosVazao([]);
            });
        }
      }
    } else {
      setDadosVazao(dados); // volta para os dados mensais
    }
  }, [origemDados, dados, vazoesDiariasCarregadas]);

  // Ordenar e filtrar os dados de vazão média ou diária
  const vazoes = dadosVazao
    .map((d) => (origemDados === 'mensal' ? d.vazaoMedia : d.vazao)) // depende do tipo escolhido
    .filter((v) => typeof v === 'number' && !isNaN(v))
    .sort((a, b) => b - a); // Ordenar do maior para o menor

  console.log(vazoes.length)

  const N = vazoes.length; // Número total de dados

  // Dados da Curva Empírica
  // Fórmula de Weibull: P = (posição / (N + 1))
  const chartData = vazoes.map((vazao, index) => ({
    permanencia: ((index + 1) / (N + 1)) * 100, // Fórmula de Weibull 
    vazao: vazao,
  }));

  // Encontrar a vazão do valor de permanência mais próximo a percentuais específicos
  const QDCalc = (d: number): number => {
    return chartData.reduce((prev, curr) =>
      Math.abs(curr.permanencia - d) < Math.abs(prev.permanencia - d) ? curr : prev
    ).vazao;
  };

  // Cálculo das vazões Q50, Q90, Q95 e Q98
  const q50 = vazoes.length > 0 ? QDCalc(50) : 0;
  const q90 = vazoes.length > 0 ? QDCalc(90) : 0;
  const q95 = vazoes.length > 0 ? QDCalc(95) : 0;
  const q98 = vazoes.length > 0 ? QDCalc(98) : 0;

  // Tabela de classes logarítmicas
  const classes = useMemo(() => {
    if (vazoes.length === 0) return [];

    const K = 30;
    const Qmax = Math.max(...vazoes);
    const Qmin = Math.min(...vazoes);
    const h = (Math.log(Qmax) - Math.log(Qmin)) / K;

    const linhas = [];
    for (let j = K; j >= 1; j--) {
      const li = Math.exp(Math.log(Qmin) + (j - 1) * h);
      const ls = Math.exp(Math.log(Qmin) + j * h);
      const fi = vazoes.filter((v) => v >= li && v < ls).length;
      const fac = vazoes.filter((v) => v >= li).length;

      linhas.push({
        classe: K - j + 1,
        li: li,
        ls: ls,
        fi: fi,
        fac: fac,
      });
    }
    return linhas;
  }, [vazoes]);

  return (
    <div>
      {/* Container principal */}
      <h2 className="text-xl font-bold mb-4">Curva de Permanência</h2>

      {/* Seção de toggle*/}
      <div className="flex justify-between items-start mb-4">
        {/* Toggle de tipo de curva */}
        <div className="flex items-center gap-4 mb-4">
          <button
            onClick={() => setTipoCurva('empirica')}
            className={`px-4 py-2 rounded ${
              tipoCurva === 'empirica'
                ? 'bg-blue-600 text-white'
                : 'bg-gray-200 text-gray-800'
            }`}
          >
            Empírica
          </button>
          <button
            onClick={() => setTipoCurva('logaritmica')}
            className={`px-4 py-2 rounded ${
              tipoCurva === 'logaritmica'
                ? 'bg-blue-600 text-white'
                : 'bg-gray-200 text-gray-800'
            }`}
          >
            Logarítmica
          </button>
        </div>

        {/* Select de tipo de dado (mensal/diaria) */}
        <div className="flex items-center gap-2">
          <label htmlFor="origemDados" className="text-sm font-medium text-gray-700">Tipo de vazão:</label>
          <select
            id="origemDados"
            value={origemDados}
            onChange={(e) => setOrigemDados(e.target.value as 'mensal' | 'diaria')}
            className="border border-gray-300 rounded px-2 py-1 text-sm"
          >
            <option value="mensal">Resumo Mensal</option>
            <option value="diaria">Vazão Diária</option>
          </select>
        </div>
      </div>

      {/* Q-values ao lado */}
      <div className="mb-4 text-sm text-gray-700 flex flex-wrap items-center gap-4">
        {/*Q values fixos*/}
        <div>  
          <span className="mr-4"><strong>Q50</strong>: {q50.toFixed(2)} m³/s</span>
          <span className="mr-4"><strong>Q90</strong>: {q90.toFixed(2)} m³/s</span>
          <span className="mr-4"><strong>Q95</strong>: {q95.toFixed(2)} m³/s</span>
          <span className="mr-4"><strong>Q98</strong>: {q98.toFixed(2)} m³/s</span>
        </div>
        {/* Q value personalizado */}
        <div className="flex items-center gap-2">
          <label htmlFor="qPersonalizado" className="text-sm">QD:</label>
          <input
            id="qCustom"
            type="number"
            min={1}
            max={99}
            value={qPersonalizado}
            onChange={(e) => {
              const value = e.target.value;
              setQPersonalizado(value);
              const num = Number(value);
              if (num >= 1 && num <= 99) {
                if (!isNaN(num)) {
                  setVazaoPersonalizada(QDCalc(num));
                } else {
                  setVazaoPersonalizada(null);
                }
              } else {
                setVazaoPersonalizada(null);
              }
            }}
            className="border border-gray-300 rounded px-2 py-1 w-16 text-sm"
          />
          {vazaoPersonalizada !== null && (
            <span className="text-sm text-blue-700 font-medium">
              Q{qPersonalizado}: {vazaoPersonalizada.toFixed(2)} m³/s
            </span>
          )}   
        </div>
      </div>

      {/* Gráfico e tabela de classes */}
      <div className="flex gap-4">
        {/* Gráfico */}
        <div className="flex-1">
          <ResponsiveContainer width="100%" height={400}>
            <LineChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis
                dataKey="permanencia"
                type="number"
                tick={{ fontSize: 10 }}
                domain={[0, 100]}
                ticks={[0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100]}
                label={{
                  value: 'Probabilidade de Permanência (%)',
                  position: 'insideBottomRight',
                  offset: -5,
                }}
              />
              <YAxis
                scale={tipoCurva === 'logaritmica' ? 'log' : 'linear'}
                domain={['auto', 'auto']}
                label={{
                  value: 'Vazão (m³/s)',
                  angle: -90,
                  position: 'insideLeft',
                }}
              />
              <Tooltip
                formatter={(value: any) => `${Number(value).toFixed(2)} m³/s`}
                labelFormatter={(label: any) => `Permanência: ${label.toFixed(1)}%`}
              />
              <Line
                type="monotone"
                dataKey="vazao"
                stroke="#3182ce"
                name="Vazão"
                dot={false}
              />
              {/* Linhas de referência dos percentuais */}
              <ReferenceLine y={q50} label="Q50" stroke="#4299e1" strokeDasharray="3 3" />
              <ReferenceLine y={q90} label="Q90" stroke="#4299e1" strokeDasharray="3 3" />
              <ReferenceLine y={q95} label="Q95" stroke="#4299e1" strokeDasharray="3 3" />
              <ReferenceLine y={q98} label="Q98" stroke="#4299e1" strokeDasharray="3 3" />

              {/*Linha de referência personalizada*/}
              {vazaoPersonalizada !== null && (
                <ReferenceLine
                  y={vazaoPersonalizada}
                  label={`Q${qPersonalizado}`}
                  stroke="#e53e3e"
                  strokeDasharray="4 2"
                />
              )}
            </LineChart>
          </ResponsiveContainer>
        </div>

        {/* Tabela de classes logarítmicas */}
        <div className="overflow-auto max-h-[400px] border rounded-md text-xs self-start">
          <table className="min-w-max text-center">
            <thead className="bg-gray-100 sticky top-0 z-10">
              <tr>
                <th className="px-2 py-1">Classe</th>
                <th className="px-2 py-1">LI (m³/s)</th>
                <th className="px-2 py-1">LS (m³/s)</th>
                <th className="px-2 py-1">Fi</th>
                <th className="px-2 py-1">Fac</th>
              </tr>
            </thead>
            <tbody>
              {classes.map((linha) => (
                <tr key={linha.classe} className="border-t">
                  <td className="px-2 py-1 font-medium">{linha.classe}</td>
                  <td className="px-2 py-1">{linha.li.toFixed(2)}</td>
                  <td className="px-2 py-1">{linha.ls.toFixed(2)}</td>
                  <td className="px-2 py-1">{linha.fi}</td>
                  <td className="px-2 py-1">{linha.fac}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
