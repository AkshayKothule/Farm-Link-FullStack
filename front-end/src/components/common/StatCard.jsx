export default function StatCard({ title, value }) {
  return (
    <div className="bg-white rounded-2xl shadow p-6">
      <p className="text-sm text-gray-500">{title}</p>
      <p className="text-3xl font-bold text-green-700 mt-2">
        {value}
      </p>
    </div>
  );
}
