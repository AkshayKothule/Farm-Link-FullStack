import { useEffect, useState } from "react";
import {
  getOwnerRentals,
  approveRental,
  rejectRental
} from "../../services/ownerService";

const PAGE_SIZE = 5;

export default function OwnerRentals() {

  const [rentals, setRentals] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(1);

  useEffect(() => {
    loadRentals();
  }, []);

  const loadRentals = async () => {
    setLoading(true);
    try {
      const res = await getOwnerRentals();
      setRentals(res.data);
      setPage(1); // reset page on reload
    } catch (err) {
      console.error("Failed to load owner rentals", err);
    } finally {
      setLoading(false);
    }
  };

  const handleApprove = async (id) => {
    await approveRental(id);
    loadRentals();
  };

  const handleReject = async (id) => {
    await rejectRental(id);
    loadRentals();
  };

  // Pagination
  const totalPages = Math.max(1, Math.ceil(rentals.length / PAGE_SIZE));
  const startIndex = (page - 1) * PAGE_SIZE;
  const paginatedRentals = rentals.slice(
    startIndex,
    startIndex + PAGE_SIZE
  );

  return (
    <div>

      <h2 className="text-xl font-semibold mb-4">
        Rental Requests & History
      </h2>

      {loading && <p>Loading...</p>}

      {!loading && paginatedRentals.length === 0 && (
        <p className="text-gray-500">No rental requests found.</p>
      )}

      <div className="space-y-4">
        {paginatedRentals.map(r => (
          <div
            key={r.rentalId}
            className="bg-white p-4 rounded shadow flex justify-between items-center"
          >
            <div>
              <h3 className="font-semibold">{r.equipmentName}</h3>
              <p className="text-sm text-gray-600">
                {r.startDate} → {r.endDate}
              </p>
              <p className="text-sm">
                Farmer: <b>{r.farmerName}</b>
              </p>

              {r.totalAmount && (
                <p className="text-green-700 font-semibold">
                  Amount: ₹{r.totalAmount}
                </p>
              )}
            </div>

            <div className="flex items-center gap-3">
              {/* STATUS BADGE */}
              <span
                className={`px-3 py-1 rounded-full text-sm font-semibold
                  ${r.status === "PENDING" && "bg-yellow-100 text-yellow-700"}
                  ${r.status === "APPROVED" && "bg-green-100 text-green-700"}
                  ${r.status === "REJECTED" && "bg-red-100 text-red-700"}
                  ${r.status === "CANCELLED" && "bg-gray-200 text-gray-700"}
                `}
              >
                {r.status}
              </span>

              {/* ACTIONS – ONLY FOR PENDING */}
              {r.status === "PENDING" && (
                <>
                  <button
                    onClick={() => handleApprove(r.rentalId)}
                    className="px-3 py-1 bg-green-600 text-white rounded"
                  >
                    Approve
                  </button>

                  <button
                    onClick={() => handleReject(r.rentalId)}
                    className="px-3 py-1 bg-red-500 text-white rounded"
                  >
                    Reject
                  </button>
                </>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Pagination */}
      {rentals.length > PAGE_SIZE && (
        <div className="flex justify-center items-center gap-4 mt-6">
          <button
            onClick={() => setPage(p => Math.max(1, p - 1))}
            disabled={page === 1}
            className="px-3 py-1 border rounded disabled:opacity-40"
          >
            Prev
          </button>

          <span className="font-semibold">
            Page {page} of {totalPages}
          </span>

          <button
            onClick={() => setPage(p => Math.min(totalPages, p + 1))}
            disabled={page === totalPages}
            className="px-3 py-1 border rounded disabled:opacity-40"
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
}
