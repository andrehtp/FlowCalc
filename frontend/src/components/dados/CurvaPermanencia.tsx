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

import { useState } from 'react';

export const CurvaPermanencia = ({ dados }: { dados: any[] }) => {
  const [tipoCurva, setTipoCurva] = useState<'empirica' | 'logaritmica'>('empirica');

  //Ordenar e filtrar os dados de vazão média
  const vazoes = dados
    .map((d) => d.vazaoMedia)
    .filter((v) => typeof v === 'number' && !isNaN(v))
    .sort((a, b) => b - a); // Ordenar do maior para o menor

  const N = vazoes.length; // Número total de dados


  // Dados da Curva Empírica
  // Fórmula de Weibull: P = (posição / (N + 1))
  const chartData = vazoes.map((vazao, index) => ({
    permanencia: ((index + 1) / (N + 1)) * 100, // Fórmula de Weibull 
    vazao: vazao,
  }));

//Encontrar a vazão do valor de permanência mais próximo a percentuais específicos
  const QDCalc = (d: number): number => {
    return chartData.reduce((prev, curr) => Math.abs(curr.permanencia - d) < Math.abs(prev.permanencia - d) ? curr : prev).vazao;
  };

  const q50 = QDCalc(50);
  const q90 = QDCalc(90);
  const q95 = QDCalc(95);
  const q98 = QDCalc(98);

  return (
     <div>
      <h2 className="text-xl font-bold mb-4">Curva de Permanência</h2>

      {/* Toggle */}
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
    {/* Q-values ao lado */}
      <div className="mb-4 text-sm text-gray-700">
        <span className="mr-4"><strong>Q50</strong>: {q50.toFixed(2)} m³/s</span>
        <span className="mr-4"><strong>Q90</strong>: {q90.toFixed(2)} m³/s</span>
        <span className="mr-4"><strong>Q95</strong>: {q95.toFixed(2)} m³/s</span>
        <span className="mr-4"><strong>Q98</strong>: {q98.toFixed(2)} m³/s</span>
      </div>

      <ResponsiveContainer width="100%" height={400}>
        <LineChart data={chartData}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis
            dataKey="permanencia"
            type='number'
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
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};