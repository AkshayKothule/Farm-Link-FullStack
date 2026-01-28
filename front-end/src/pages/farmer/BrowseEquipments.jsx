import { useEffect, useState } from "react";
import api from "../../services/api";
import EquipmentCard from "../../components/equipments/EquipmentCard";

const ITEMS_PER_PAGE = 6;

export default function BrowseEquipments() {
  const [equipments, setEquipments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    setLoading(true);
    api.get("/farmers/equipments/available")
      .then(res => {
        setEquipments(res.data || []);
        setCurrentPage(1);
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  // ================= PAGINATION =================
  const totalPages = Math.ceil(equipments.length / ITEMS_PER_PAGE);

  const paginatedEquipments = equipments.slice(
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

  return (
    <div className=" bg-green-50 p-6 sm:p-8">

      {/* ===== HEADER CARD ===== */}
      <div className="bg-white rounded-2xl shadow p-6 mb-8">
        <h1 className="text-3xl font-bold text-green-900">
          Browse Equipment 🚜
        </h1>
        <p className="text-gray-600 mt-1">
          Choose the right equipment for your farming needs
        </p>
      </div>

      {/* ===== CONTENT ===== */}
      {loading ? (
        <div className="text-green-700 font-semibold">
          Loading equipments...
        </div>
      ) : equipments.length === 0 ? (
        <p className="text-gray-500">
          No equipment available at the moment.
        </p>
      ) : (
        <>
          {/* GRID */}
          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {paginatedEquipments.map(eq => (
              <EquipmentCard key={eq.id} equipment={eq} />
            ))}
          </div>

          {/* PAGINATION */}
          {totalPages > 1 && (
            <div className="flex items-center justify-center gap-4 mt-10">
              <button
                onClick={prevPage}
                disabled={currentPage === 1}
                className="
                  px-4 py-2 border rounded-lg text-sm font-semibold
                  disabled:opacity-50 bg-white
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
                  disabled:opacity-50 bg-white
                "
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
