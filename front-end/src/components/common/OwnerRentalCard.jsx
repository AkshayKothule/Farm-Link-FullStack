import api from "../../services/api";

export default function OwnerRentalCard({ rental, onAction }) {

  const approve = () => {
    api.put(`/rentals/owner/${rental.rentalId}/approve`)
      .then(onAction);
  };

  const reject = () => {
    api.put(`/rentals/owner/${rental.rentalId}/reject`)
      .then(onAction);
  };

  return (
    <div className="border rounded-xl p-4 flex justify-between items-center">
      <div>
        <h4 className="font-semibold">{rental.equipmentName}</h4>
        <p className="text-xs text-gray-500">
          {rental.startDate} → {rental.endDate}
        </p>
        <p className="text-sm mt-1">
          Farmer: {rental.farmerName}
        </p>
      </div>

      <div className="flex gap-2">
        {rental.status === "PENDING" && (
          <>
            <button
              onClick={approve}
              className="bg-green-600 text-white px-3 py-1 rounded"
            >
              Approve
            </button>
            <button
              onClick={reject}
              className="bg-red-600 text-white px-3 py-1 rounded"
            >
              Reject
            </button>
          </>
        )}

        {rental.status !== "PENDING" && (
          <span className="text-sm font-semibold text-gray-600">
            {rental.status}
          </span>
        )}
      </div>
    </div>
  );
}
