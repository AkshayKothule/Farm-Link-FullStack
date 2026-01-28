import { useEffect, useState } from "react";
import { getOwners, verifyOwner } from "../../services/adminService";
import { successToast, errorToast } from "../../utils/toast";

export default function AdminOwners() {
  const [owners, setOwners] = useState([]);

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    try {
      const res = await getOwners();
      setOwners(res.data || []);
    } catch {
      errorToast("Failed to load owners");
    }
  };

  const handleVerify = async (id) => {
    try {
      await verifyOwner(id);
      successToast("Owner verified");
      load();
    } catch {
      errorToast("Verification failed");
    }
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Owners 👨‍🌾</h1>

      <div className="bg-white rounded-xl shadow overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-gray-100">
            <tr>
              <th className="p-3 text-left">Name</th>
              <th className="p-3 text-left">Business</th>
              <th className="p-3">Status</th>
              <th className="p-3">Action</th>
            </tr>
          </thead>

          <tbody>
            {owners.map(o => (
              <tr key={o.id} className="border-t">
                <td className="p-3">{o.userName}</td>
                <td className="p-3">{o.businessName}</td>
                <td className="p-3">
                  {o.verified ? "✅ Verified" : "❌ Pending"}
                </td>
                <td className="p-3 text-center">
                  {!o.verified && (
                    <button
                      onClick={() => handleVerify(o.id)}
                      className="bg-green-600 text-white px-3 py-1 rounded"
                    >
                      Verify
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
