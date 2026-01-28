import { useEffect, useState } from "react";
import { getFarmers } from "../../services/adminService";
import { errorToast } from "../../utils/toast";

export default function AdminFarmers() {
  const [farmers, setFarmers] = useState([]);

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    try {
      const res = await getFarmers();
      setFarmers(res.data || []);
    } catch {
      errorToast("Failed to load farmers");
    }
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">
        Farmers 👨‍🌾
      </h1>

      <div className="bg-white rounded-xl shadow overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-gray-100">
            <tr>
              <th className="p-3 text-left">Name</th>
              <th className="p-3 text-left">Email</th>
            </tr>
          </thead>

          <tbody>
            {farmers.map(f => (
              <tr key={f.id} className="border-t">
                <td className="p-3">{f.name}</td>
                <td className="p-3">{f.email}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
