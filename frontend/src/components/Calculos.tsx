import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ReferenceLine,
  ResponsiveContainer,
} from 'recharts';
import { quantile } from 'simple-statistics';

export const Calculos = ({ dados }: { dados: any[] }) => {
  const mediasNormais = dados
    .map((d) => d.vazaoMedia)
    .filter((v) => typeof v === 'number' && !isNaN(v));

  const mediasReais = dados
    .map((d) => d.vazaoMediaReal)
    .filter((v) => typeof v === 'number' && !isNaN(v));

  // Percentis - média normal
  const q50 = quantile(mediasNormais, 0.50);
  const q90 = quantile(mediasNormais, 0.90);
  const q95 = quantile(mediasNormais, 0.95);
  const q98 = quantile(mediasNormais, 0.98);

  // Percentis - média real
  const q50r = quantile(mediasReais, 0.50);
  const q90r = quantile(mediasReais, 0.90);
  const q95r = quantile(mediasReais, 0.95);
  const q98r = quantile(mediasReais, 0.98);

    const chartData = dados
    .map((d) => ({
        data: d.data,
        vazaoMedia: d.vazaoMedia,
        vazaoMediaReal: d.vazaoMediaReal,
    }))
    .filter((d) =>
        typeof d.vazaoMedia === 'number' &&
        typeof d.vazaoMediaReal === 'number' &&
        !isNaN(d.vazaoMedia) &&
        !isNaN(d.vazaoMediaReal)
    )
    .sort((a, b) => a.vazaoMedia - b.vazaoMedia);

  return (
    <div>
      <h2 className="text-xl font-bold mb-4">Gráfico Percentil</h2>

      <ResponsiveContainer width="35%" height={300}>
        <LineChart data={chartData}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="data" tick={{ fontSize: 10 }} />
          <YAxis />
          <Tooltip />
          <Legend />

          {/* Linhas de série */}
          <Line
            type="monotone"
            dataKey="vazaoMedia"
            stroke="#3182ce"
            name="Vazão Média (normal)"
            dot={false}
          />
          <Line
            type="monotone"
            dataKey="vazaoMediaReal"
            stroke="#2f855a"
            name="Vazão Média (real)"
            dot={false}
          />

          {/* Percentis - Normal */}
          <ReferenceLine y={q50} label="Q50" stroke="#4299e1" strokeDasharray="3 3" />
          <ReferenceLine y={q90} label="Q90" stroke="#4299e1" strokeDasharray="3 3" />
          <ReferenceLine y={q95} label="Q95" stroke="#4299e1" strokeDasharray="3 3" />
          <ReferenceLine y={q98} label="Q98" stroke="#4299e1" strokeDasharray="3 3" />

          {/* Percentis - Real */}
          <ReferenceLine y={q50r} label="Q50r" stroke="#38a169" strokeDasharray="3 3" />
          <ReferenceLine y={q90r} label="Q90r" stroke="#38a169" strokeDasharray="3 3" />
          <ReferenceLine y={q95r} label="Q95r" stroke="#38a169" strokeDasharray="3 3" />
          <ReferenceLine y={q98r} label="Q98r" stroke="#38a169" strokeDasharray="3 3" />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};
