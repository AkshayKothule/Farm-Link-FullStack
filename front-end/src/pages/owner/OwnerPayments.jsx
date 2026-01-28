import { useEffect, useState } from "react";
import { getOwnerPayments } from "../../services/ownerService";

const ITEMS_PER_PAGE = 5;

export default function OwnerPayments() {
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(1);

  useEffect(() => {
    loadPayments();
  }, []);

  const loadPayments = async () => {
    try {
      const res = await getOwnerPayments();
      setPayments(res.data || []);
    } catch (err) {
      console.error("Failed to load payments", err);
    } finally {
      setLoading(false);
    }
  };

  // ===== SUMMARY =====
  const totalEarnings = payments.reduce(
    (sum, p) => sum + (p.amount || 0),
    0
  );

  // ===== PAGINATION =====
  const totalPages = Math.ceil(payments.length / ITEMS_PER_PAGE);
  const paginated = payments.slice(
    (page - 1) * ITEMS_PER_PAGE,
    page * ITEMS_PER_PAGE
  );

  return (
    <div>
      {/* HEADER */}
      <h1 className="text-2xl font-bold mb-1">Payments 💰</h1>
      <p className="text-gray-500 mb-6">
        Payments received from farmers
      </p>

      {/* SUMMARY CARD */}
      <div className="bg-white rounded-xl shadow p-5 mb-6">
        <p className="text-sm text-gray-500">Total Earnings</p>
        <p className="text-2xl font-bold text-green-700">
          ₹ {totalEarnings}
        </p>
      </div>

      {/* CONTENT */}
      {loading ? (
        <p>Loading payments...</p>
      ) : payments.length === 0 ? (
        <p className="text-gray-500">No payments received yet.</p>
      ) : (
        <>
          <div className="bg-white rounded-xl shadow overflow-hidden">
            <table className="w-full text-sm">
              <thead className="bg-green-100 text-green-900">
                <tr>
                  <th className="p-3 text-left">Equipment</th>
                  <th className="p-3 text-left">Farmer</th>
                  <th className="p-3 text-left">Amount</th>
                  <th className="p-3 text-left">Status</th>
                  <th className="p-3 text-left">Paid On</th>
                </tr>
              </thead>
              <tbody>
                {paginated.map((p, i) => (
                  <tr key={i} className="border-t">
                    <td className="p-3">{p.equipmentName}</td>
                    <td className="p-3">{p.farmerName}</td>
                    <td className="p-3 font-semibold text-green-700">
                      ₹ {p.amount}
                    </td>
                    <td className="p-3">
                      <span className="px-2 py-1 rounded-full text-xs bg-green-100 text-green-700">
                        {p.status}
                      </span>
                    </td>
                    <td className="p-3 text-gray-500">
                      {new Date(p.paidAt).toLocaleDateString()}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* PAGINATION */}
          {totalPages > 1 && (
            <div className="flex justify-center gap-4 mt-6">
              <button
                disabled={page === 1}
                onClick={() => setPage(p => p - 1)}
                className="px-4 py-2 border rounded disabled:opacity-50"
              >
                ← Prev
              </button>
              <span className="text-sm font-semibold">
                Page {page} of {totalPages}
              </span>
              <button
                disabled={page === totalPages}
                onClick={() => setPage(p => p + 1)}
                className="px-4 py-2 border rounded disabled:opacity-50"
              >
                Next →
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
}
