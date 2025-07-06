import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { ResumoHidrologico } from '../components/dados/ResumoHidrologico';
import { CurvaPermanencia } from '../components/dados/CurvaPermanencia';
import { Mapa } from '../components/dados/Mapa';

export const DadosPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [dados, setDados] = useState<any>(location.state?.dados ?? null);
  const [mensagemErro, setMensagemErro] = useState<string | null>(null);

  const [abaAtiva, setAbaAtiva] = useState<'resumo' | 'curva'>('resumo');
  const [nivelConsistencia, setNivelConsistencia] = useState<string | null>(null);
  const [dataInicioFixada, setDataInicioFixada] = useState<string | null>(null);
  const [dataFimFixada, setDataFimFixada] = useState<string | null>(null);

  const [nivelConsistenciaInput, setNivelConsistenciaInput] = useState<string | null>(null);
  const [dataInicioInput, setDataInicioInput] = useState<string | null>(null);
  const [dataFimInput, setDataFimInput] = useState<string | null>(null);

  const cabecalho = dados?.cabecalho ?? {};
  const resumosMensais = dados?.resumosMensais ?? [];
  const dadosFiltrados = resumosMensais;

  useEffect(() => {
    if (!location.state?.dados) {
      navigate('/', { replace: true });
    }
  }, [location.state, navigate]);

  useEffect(() => {
    if (resumosMensais.length > 0 && !dataInicioFixada && !dataFimFixada) {
      const ordenadas = [...resumosMensais].sort((a, b) =>
        new Date(a.dataInicial).getTime() - new Date(b.dataInicial).getTime()
      );
      const min = ordenadas[0].dataInicial.split('T')[0];
      const max = ordenadas.at(-1).dataInicial.split('T')[0];

      setDataInicioFixada(min);
      setDataFimFixada(max);

      setDataInicioInput(min);
      setDataFimInput(max);
      setNivelConsistenciaInput(nivelConsistencia);
    }
  }, [resumosMensais]);

  const handleFiltrar = () => {
    if (!cabecalho?.codigoEstacao || !dataInicioInput || !dataFimInput) return;

    const body = {
      codEstacao: cabecalho.codigoEstacao,
      dataInicio: dataInicioInput,
      dataFim: dataFimInput,
      nivelConsistencia: nivelConsistenciaInput
    };

    fetch('http://localhost:8080/api/estacoes/consulta-estacao', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    })
      .then(async res => {
        const texto = await res.text();
        try {
          const data = JSON.parse(texto);
          setDados(data);
          setNivelConsistencia(nivelConsistenciaInput);
          setDataInicioFixada(dataInicioInput);
          setDataFimFixada(dataFimInput);
          setMensagemErro(null);
        } catch (e) {
          setMensagemErro("Não existem dados para estes filtros no período específicado. Valores anteriores foram mantidos na página.");
        }
      })
      .catch((err) => {
        console.error(err);
        setMensagemErro("Erro ao consultar o backend.");
      });
  };

  return (
    <div className="min-h-screen bg-gray-100 pt-2 pl-4 pr-4 pb-11">
      <div className="flex gap-6">
        {/* Coluna esquerda */}
        <div className="flex flex-col gap-6 w-[350px]">
          <div className="bg-[#e7eaf6] rounded-lg shadow-md p-6">
            <h1 className="text-lg font-semibold mb-4">Filtros</h1>

            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-1">Nível de consistência</label>
              <select
                className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                value={nivelConsistenciaInput || ''}
                onChange={(e) => setNivelConsistenciaInput(e.target.value || null)}
              >
                <option value="">Todos</option>
                <option value="1">1 - Bruto</option>
                <option value="2">2 - Consistido</option>
              </select>
            </div>

            <div className="flex gap-4 mb-4">
              <div className="flex-1">
                <label className="block text-sm font-medium text-gray-700 mb-1">De</label>
                <input
                  type="date"
                  className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                  value={dataInicioInput || ''}
                  onChange={(e) => setDataInicioInput(e.target.value)}
                />
              </div>

              <div className="flex-1">
                <label className="block text-sm font-medium text-gray-700 mb-1">Até</label>
                <input
                  type="date"
                  className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                  value={dataFimInput || ''}
                  onChange={(e) => setDataFimInput(e.target.value)}
                />
              </div>
            </div>

            <button
              onClick={handleFiltrar}
              className="w-full bg-blue-600 text-white text-sm py-2 rounded hover:bg-blue-700 transition"
            >
              Filtrar
            </button>

            {mensagemErro && (
              <div className="mt-4 p-3 rounded bg-red-100 text-red-700 text-sm font-medium border border-red-300">
                {mensagemErro}
              </div>
            )}
          </div>

          <div className="bg-[#e7eaf6] rounded-lg shadow-md p-6">
            <h1 className="text-lg font-semibold mb-4">Informações da estação</h1>
            <ul className="space-y-3 text-sm text-gray-800 break-words">
              <li>Código da estação: {mostrarOuIndisponivel(cabecalho.codigoEstacao)}</li>
              <li>Nome da estação: {mostrarOuIndisponivel(cabecalho.nomeEstacao)}</li>
              <li>Estado: {mostrarOuIndisponivel(cabecalho.nomeEstado)}</li>
              <li>Cidade: {mostrarOuIndisponivel(cabecalho.nomeCidade)}</li>
              <li>Rio: {mostrarOuIndisponivel(cabecalho.nomeRio)}</li>
              <li>Bacia: {mostrarOuIndisponivel(cabecalho.codigoBacia)}</li>
              <li>Sub-bacia: {mostrarOuIndisponivel(cabecalho.codigoSubBacia)}</li>
              <li>Latitude: {mostrarOuIndisponivel(cabecalho.latitudeEstacao)}</li>
              <li>Longitude: {mostrarOuIndisponivel(cabecalho.longitudeEstacao)}</li>
              <li>Altitude: {mostrarOuIndisponivel(cabecalho.altitudeEstacao)}</li>
            </ul>
            {cabecalho.latitudeEstacao && cabecalho.longitudeEstacao &&
              <Mapa latitude={cabecalho.latitudeEstacao} longitude={cabecalho.longitudeEstacao} />}
          </div>
        </div>

        {/* Painel principal */}
        <div className="bg-white rounded-lg shadow-md p-6 flex-1">
          <div className="flex items-center gap-6 mb-6 border-b border-gray-300">
            <button
              onClick={() => setAbaAtiva('resumo')}
              className={`text-lg font-semibold pb-2 ${abaAtiva === 'resumo' ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-600'}`}
            >
              Dados
            </button>
            <button
              onClick={() => setAbaAtiva('curva')}
              className={`text-lg font-semibold pb-2 ${abaAtiva === 'curva' ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-600'}`}
            >
              Curva de Permanência
            </button>
          </div>

          {abaAtiva === 'resumo' && <ResumoHidrologico dados={dadosFiltrados} />}
          {abaAtiva === 'curva' && (
            <CurvaPermanencia
              codigoEstacao={cabecalho.codigoEstacao}
              dataInicio={dataInicioFixada!}
              dataFim={dataFimFixada!}
              nivelConsistencia={nivelConsistencia}
            />
          )}
        </div>
      </div>
    </div>
  );
};

const mostrarOuIndisponivel = (valor?: string | number) =>
  valor !== null && valor !== undefined && String(valor).trim() !== ''
    ? valor
    : 'Não disponível';
