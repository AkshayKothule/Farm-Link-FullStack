import { useEffect, useState } from "react";
import api from "../../services/api";

const PaymentHistory = () => {
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    api
      .get("/payments/farmer")
      .then((res) => {
        setPayments(res.data || []);
      })
      .catch(() => {
        setError("Failed to load payment history");
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <p className="text-green-700 font-semibold">
        Loading payment history...
      </p>
    );
  }

  if (error) {
    return (
      <p className="text-red-600 font-semibold">
        {error}
      </p>
    );
  }

  return (
    <>
      <h1 className="text-3xl font-bold text-green-900 mb-6">
        Payment History 💳
      </h1>

      {payments.length === 0 ? (
        <p className="text-gray-500">
          You haven’t made any payments yet.
        </p>
      ) : (
        <div className="bg-white rounded-2xl shadow overflow-hidden">
          <table className="w-full text-sm">
            <thead className="bg-green-100 text-green-900">
              <tr>
                <th className="p-3 text-left">Equipment</th>
                <th className="p-3 text-center">Amount</th>
                <th className="p-3 text-center">Status</th>
                <th className="p-3 text-center">Paid At</th>
              </tr>
            </thead>

            <tbody>
              {payments.map((p, index) => (
                <tr
                  key={index}
                  className="border-t hover:bg-gray-50 transition"
                >
                  <td className="p-3">
                    {p.equipmentName}
                  </td>

                  <td className="p-3 text-center font-semibold">
                    ₹ {p.amount}
                  </td>

                  <td className="p-3 text-center">
                    <span
                      className={`px-3 py-1 rounded-full text-xs font-semibold ${
                        p.status === "SUCCESS"
                          ? "bg-green-100 text-green-700"
                          : p.status === "FAILED"
                          ? "bg-red-100 text-red-700"
                          : "bg-yellow-100 text-yellow-700"
                      }`}
                    >
                      {p.status}
                    </span>
                  </td>

                  <td className="p-3 text-center text-gray-500">
                    {p.paidAt
                      ? new Date(p.paidAt).toLocaleString()
                      : "-"}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </>
  );
};

export default PaymentHistory;
