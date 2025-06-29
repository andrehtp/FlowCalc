import { useState, useMemo } from 'react';

type LinhaResumo = {
  dataInicial: string;
  resumoMensalId: number;
  vazaoMedia: number;
  vazaoMaxima: number;
  vazaoMinima: number;
  vazaoMediaReal: number;
  vazaoMaximaReal: number;
  vazaoMinimaReal: number;
  nivelConsistencia: number;
  metodoObtencao: number;
};

type ResumoHidrologicoProps = {
  dados: LinhaResumo[];
};

export const ResumoHidrologico = ({ dados }: ResumoHidrologicoProps) => {
  const [paginaAtual, setPaginaAtual] = useState(1);
  const porPagina = 15;
  const totalPaginas = Math.ceil(dados.length / porPagina);
  const inicio = (paginaAtual - 1) * porPagina;
  const dadosPagina = dados.slice(inicio, inicio + porPagina);

  const [dataMinima, dataMaxima] = useMemo(() => {
    if (!dados.length) return [null, null];

    const ordenadas = [...dados].sort(
      (a, b) =>
        new Date(a.dataInicial).getTime() - new Date(b.dataInicial).getTime()
    );

    return [
      ordenadas[0].dataInicial,
      ordenadas[ordenadas.length - 1].dataInicial,
    ];
  }, [dados]);

  return (
    <div className="bg-white p-4 rounded shadow">
      {/* Título principal */}
      <h2 className="text-lg font-semibold mb-4">
        Cálculos totais no período de{' '}
        <span className="font-normal">
          {formatarDataBR(dataMinima)} até {formatarDataBR(dataMaxima)}
        </span>
      </h2>

      {/* Cards estatísticos */}
      <div className="grid grid-cols-2 md:grid-cols-10 gap-4 mb-6">
        <div className="bg-blue-50 rounded-md p-2 text-center shadow-sm border border-blue-100">
          <p className="text-[10px] text-gray-600">Resumos Mensais</p>
          <p className="text-base font-semibold">{dados.length > 0 ? dados.length : '-'}</p>
        </div>

        <div className="bg-blue-50 rounded-md p-2 text-center shadow-sm border border-blue-100">
          <p className="text-[10px] text-gray-600">Vazão Máxima</p>
          <p className="text-base font-semibold">
            {dados.length > 0
              ? Math.max(...dados.map((d) => d.vazaoMaxima ?? -Infinity)) !== -Infinity
                ? Math.max(...dados.map((d) => d.vazaoMaxima ?? -Infinity)).toFixed(2)
                : '-'
              : '-'}
          </p>
        </div>

        <div className="bg-blue-50 rounded-md p-2 text-center shadow-sm border border-blue-100">
          <p className="text-[10px] text-gray-600">Vazão Média</p>
          <p className="text-base font-semibold">
            {dados.length > 0
              ? (
                  dados.reduce((acc, d) => acc + (d.vazaoMedia ?? 0), 0) /
                  dados.filter((d) => d.vazaoMedia !== undefined && d.vazaoMedia !== null).length
                ).toFixed(2)
              : '-'}
          </p>
        </div>

        <div className="bg-blue-50 rounded-md p-2 text-center shadow-sm border border-blue-100">
          <p className="text-[10px] text-gray-600">Vazão Mínima</p>
          <p className="text-base font-semibold">
            {dados.length > 0
              ? Math.min(...dados.map((d) => d.vazaoMinima ?? Infinity)) !== Infinity
                ? Math.min(...dados.map((d) => d.vazaoMinima ?? Infinity)).toFixed(2)
                : '-'
              : '-'}
          </p>
        </div>

        <div className="bg-blue-50 rounded-md p-2 text-center shadow-sm border border-blue-100">
          <p className="text-[10px] text-gray-600">Relatórios</p>
          <p className="text-base font-semibold">{dados.length > 0 ? dados.length : '-'}</p>
        </div>
      </div>
      
      {/*Tabela de dados*/}
      <h2 className="text-lg font-semibold mb-4">Vazões mensais</h2>
      <div className="overflow-x-auto">
        <table className="min-w-full text-sm border-collapse border">
          <thead className="bg-gray-200 text-gray-700">
            <tr>
              <th className="border px-2 py-1">Data</th>
              <th className="border px-2 py-1">Média</th>
              <th className="border px-2 py-1">Máxima</th>
              <th className="border px-2 py-1">Mínima</th>
              <th className="border px-2 py-1">Média Real</th>
              <th className="border px-2 py-1">Máxima Real</th>
              <th className="border px-2 py-1">Mínima Real</th>
              <th className="border px-2 py-1">Consistência</th>
            </tr>
          </thead>
          <tbody>
            {dadosPagina.map((linha, index) => (
              <tr key={index} className="text-center">
                <td className="border px-2 py-1">
                  {formatarDataBR(linha.dataInicial)}
                </td>
                <td className="border px-2 py-1">
                  {linha.vazaoMedia ?? '-'}
                </td>
                <td className="border px-2 py-1">
                  {linha.vazaoMaxima ?? '-'}
                </td>
                <td className="border px-2 py-1">
                  {linha.vazaoMinima ?? '-'}
                </td>
                <td className="border px-2 py-1">
                  {linha.vazaoMediaReal ?? '-'}
                </td>
                <td className="border px-2 py-1">
                  {linha.vazaoMaximaReal ?? '-'}
                </td>
                <td className="border px-2 py-1">
                  {linha.vazaoMinimaReal ?? '-'}
                </td>
                <td className="border px-2 py-1">
                  {linha.nivelConsistencia ?? '-'}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Paginação */}
      <div className="flex items-center justify-between mt-4 text-sm">
        <button
          onClick={() => setPaginaAtual((p) => Math.max(1, p - 1))}
          disabled={paginaAtual === 1}
          className="px-2 py-1 bg-gray-200 rounded disabled:opacity-50"
        >
          Anterior
        </button>
        <span>
          Página {paginaAtual} de {totalPaginas} ({dados?.length})
        </span>
        <button
          onClick={() => setPaginaAtual((p) => Math.min(totalPaginas, p + 1))}
          disabled={paginaAtual === totalPaginas}
          className="px-2 py-1 bg-gray-200 rounded disabled:opacity-50"
        >
          Próxima
        </button>
      </div>
    </div>
  );
};

const formatarDataBR = (iso: string | null) => {
  if (!iso) return '-';
  return new Intl.DateTimeFormat('pt-BR').format(new Date(iso));
};
