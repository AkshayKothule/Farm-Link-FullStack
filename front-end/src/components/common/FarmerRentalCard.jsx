import { useState } from "react";
import api from "../../services/api";
import { errorToast } from "../../utils/toast";

export default function FarmerRentalCard({ rental, onCancel, onPay }) {
  const [loading, setLoading] = useState(false);
  const rentalId = rental?.rentalId || rental?.id;

  if (!rentalId) return null;

  const cancelRental = async () => {
    if (!window.confirm("Cancel this rental request?")) return;
    if (loading) return;

    setLoading(true);
    try {
      await api.delete(`/rentals/farmer/${rentalId}`);
      errorToast("Rental cancelled ❌");
      onCancel();
    } catch (err) {
      errorToast(err.response?.data || "Failed to cancel rental");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-gray-50 border rounded-xl p-4 flex justify-between items-center">
      <div>
        <h4 className="font-semibold text-green-900">
          {rental.equipmentName}
        </h4>
        <p className="text-xs text-gray-500">
          {rental.startDate} → {rental.endDate}
        </p>
        <p className="text-sm mt-1">
          Owner: <b>{rental.ownerName}</b>
        </p>
      </div>

      <div className="flex items-center gap-3">
        <span className="px-3 py-1 rounded-full text-xs font-semibold bg-yellow-100 text-yellow-700">
          {rental.status}
        </span>

        {rental.status === "PENDING" && (
          <button
            onClick={cancelRental}
            disabled={loading}
            className="bg-red-600 text-white px-3 py-1 rounded text-xs"
          >
            Cancel
          </button>
        )}

        {rental.status === "APPROVED" && (
          <button
            onClick={() => onPay(rental)}
            className="bg-green-600 text-white px-3 py-1 rounded text-xs"
          >
            Pay 💳
          </button>
        )}
      </div>
    </div>
  );
}
