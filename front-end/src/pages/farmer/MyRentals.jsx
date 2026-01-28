import { useEffect, useState } from "react";
import api from "../../services/api";
import FarmerRentalCard from "../../components/common/FarmerRentalCard";
import PaymentModal from "../../components/modals/PaymentModal";
import { errorToast } from "../../utils/toast";

const ITEMS_PER_PAGE = 4;

export default function MyRentals() {
  const [rentals, setRentals] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedRental, setSelectedRental] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);

  // ================= LOAD FARMER RENTALS =================
  const loadRentals = () => {
    setLoading(true);

    api.get("/rentals/farmer")
      .then(res => {
        setRentals(Array.isArray(res.data) ? res.data : []);
        setCurrentPage(1);
      })
      .catch(err => {
        console.error("Failed to load farmer rentals", err);
        errorToast(err.response?.data || "Failed to load rentals");
        setRentals([]);
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadRentals();
  }, []);

  // ================= PAGINATION =================
  const totalPages = Math.max(
    1,
    Math.ceil(rentals.length / ITEMS_PER_PAGE)
  );

  const paginatedRentals = rentals.slice(
    (currentPage - 1) * ITEMS_PER_PAGE,
    currentPage * ITEMS_PER_PAGE
  );

  const nextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(p => p + 1);
    }
  };

  const prevPage = () => {
    if (currentPage > 1) {
      setCurrentPage(p => p - 1);
    }
  };

  // ================= UI =================
  if (loading) {
    return (
      <p className="text-green-700 font-semibold">
        Loading rentals...
      </p>
    );
  }

  return (
    <>
      <h1 className="text-3xl font-bold text-green-900 mb-6">
        My Rental Requests 📋
      </h1>

      {rentals.length === 0 ? (
        <p className="text-gray-500">
          No rental requests yet.
        </p>
      ) : (
        <>
          {/* RENTAL LIST */}
          <div className="space-y-4">
            {paginatedRentals.map(rental => (
              <FarmerRentalCard
                key={rental.rentalId || rental.id}
                rental={rental}
                onCancel={loadRentals}
                onPay={() => setSelectedRental(rental)}
              />
            ))}
          </div>

          {/* PAGINATION CONTROLS */}
          <div className="flex items-center justify-center gap-4 mt-8">
            <button
              onClick={prevPage}
              disabled={currentPage === 1}
              className="
                px-4 py-2 border rounded-lg text-sm font-semibold
                disabled:opacity-50
              "
            >
              ← Previous
            </button>

            <span className="text-sm font-medium text-gray-700">
              Page {currentPage} of {totalPages}
            </span>

            <button
              onClick={nextPage}
              disabled={currentPage === totalPages}
              className="
                px-4 py-2 border rounded-lg text-sm font-semibold
                disabled:opacity-50
              "
            >
              Next →
            </button>
          </div>
        </>
      )}

      {/* PAYMENT MODAL */}
      {selectedRental && (
        <PaymentModal
          rental={selectedRental}
          onClose={() => setSelectedRental(null)}
          onSuccess={() => {
            setSelectedRental(null);
            loadRentals();
          }}
        />
      )}
    </>
  );
}
