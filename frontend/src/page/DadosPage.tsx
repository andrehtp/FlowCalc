import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { ResumoHidrologico } from '../components/dados/ResumoHidrologico';
import { CurvaPermanencia } from '../components/dados/CurvaPermanencia';
import { Mapa } from '../components/dados/Mapa';

export const DadosPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const dados = location.state?.dados;

  const [abaAtiva, setAbaAtiva] = useState<'resumo' | 'curva'>('resumo');
  const [nivelConsistencia, setNivelConsistencia] = useState<string | null>(null);

  // Bloqueia acesso direto pela URL
  useEffect(() => {
    if (!location.state || !location.state.dados) {
      navigate('/', { replace: true });
    }
  }, [location.state, navigate]);

  const cabecalho = dados?.cabecalho ?? {};
  const resumosMensais = dados?.resumosMensais ?? [];

  const dadosFiltrados = nivelConsistencia
    ? resumosMensais.filter((d: any) => String(d.nivelConsistencia) === nivelConsistencia)
    : resumosMensais;

  return (
    <div className="min-h-screen bg-gray-100 pt-2 pl-4 pr-4 pb-11">
      <div className="flex gap-6">
        {/* Coluna esquerda */}
        <div className="flex flex-col gap-6 w-[350px]">
          {/* Card Filtros */}
          <div className="bg-[#e7eaf6] rounded-lg shadow-md p-6">
            <h1 className="text-lg font-semibold mb-4">Filtros</h1>

            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-1">Nível de consistência</label>
              <select
                className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                value={nivelConsistencia || ''}
                onChange={(e) =>
                  setNivelConsistencia(e.target.value !== '' ? e.target.value : null)
                }
              >
                <option value="">Todos</option>
                <option value="1">1 - Bruto</option>
                <option value="2">2 - Consistido</option>
              </select>
            </div>
          </div>

          {/* Card Informações */}
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
              <Mapa latitude={cabecalho.latitudeEstacao} longitude={cabecalho.longitudeEstacao} />
            }
          </div>
        </div>

        {/* Card que ocupa o restante */}
        <div className="bg-white rounded-lg shadow-md p-6 flex-1">
          {/* Botões de navegação entre abas */}
          <div className="flex items-center gap-6 mb-6 border-b border-gray-300">
            <button
              onClick={() => setAbaAtiva('resumo')}
              className={`text-lg font-semibold pb-2 ${abaAtiva === 'resumo'
                ? 'border-b-2 border-blue-600 text-blue-600'
                : 'text-gray-600'}`}
            >
              Dados
            </button>

            <button
              onClick={() => setAbaAtiva('curva')}
              className={`text-lg font-semibold pb-2 ${abaAtiva === 'curva'
                ? 'border-b-2 border-blue-600 text-blue-600'
                : 'text-gray-600'}`}
            >
              Curva de Permanência
            </button>
          </div>

          {/* Conteúdo que muda conforme a aba ativa */}
          {abaAtiva === 'resumo' && <ResumoHidrologico dados={dadosFiltrados} />}
          {abaAtiva === 'curva' && <CurvaPermanencia dados={dadosFiltrados} />}
        </div>
      </div>
    </div>
  );
};

const mostrarOuIndisponivel = (valor?: string | number) =>
  valor !== null && valor !== undefined && String(valor).trim() !== ''
    ? valor
    : 'Não disponível';
