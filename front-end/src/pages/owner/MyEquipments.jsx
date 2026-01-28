import { useEffect, useState } from "react";
import {
  getMyEquipments,
  deleteEquipment,
} from "../../services/equipmentService";
import AddEquipmentModal from "../../components/modals/AddEquipmentModal";
import { errorToast, successToast } from "../../utils/toast";
import ImageCarousel from "../../components/common/ImageCarousel";

const ITEMS_PER_PAGE = 6;
const PLACEHOLDER = "/placeholder-equipment.jpg";

export default function MyEquipments() {
  const [equipments, setEquipments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showAddModal, setShowAddModal] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    loadEquipments();
  }, []);

  const loadEquipments = async () => {
    try {
      setLoading(true);
      const res = await getMyEquipments();
      setEquipments(res.data || []);
      setCurrentPage(1);
    } catch (err) {
      errorToast("Failed to load equipments");
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this equipment?")) return;

    try {
      await deleteEquipment(id);
      successToast("Equipment removed");
      loadEquipments();
    } catch (err) {
      errorToast(
        err.response?.data?.message ||
        "Cannot delete equipment"
      );
    }
  };

  // ✅ show only available
  const availableEquipments = equipments.filter(eq => eq.available);

  // ================= PAGINATION =================
  const totalPages = Math.ceil(
    availableEquipments.length / ITEMS_PER_PAGE
  );

  const paginatedEquipments = availableEquipments.slice(
    (currentPage - 1) * ITEMS_PER_PAGE,
    currentPage * ITEMS_PER_PAGE
  );

  return (
    <div>
      {/* HEADER */}
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-2xl font-bold mb-1">
            My Equipments 🚜
          </h1>
          <p className="text-gray-500">
            Only active equipments are shown here
          </p>
        </div>

        <button
          onClick={() => setShowAddModal(true)}
          className="bg-green-700 text-white px-4 py-2 rounded-lg font-semibold hover:bg-green-800"
        >
          + Add Equipment
        </button>
      </div>

      {/* CONTENT */}
      {loading ? (
        <p className="text-gray-500">Loading equipments...</p>
      ) : availableEquipments.length === 0 ? (
        <div className="bg-white rounded-xl p-6 text-center text-gray-500">
          No active equipments available.
        </div>
      ) : (
        <>
          {/* GRID */}
          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {paginatedEquipments.map(eq => (
              <div
  key={eq.id}
  className="bg-white rounded-2xl shadow overflow-hidden flex flex-col"
>
  {/* IMAGE CAROUSEL */}
  <ImageCarousel images={eq.imageUrls} />

  <div className="p-5 flex flex-col flex-1">
    <h3 className="text-lg font-semibold text-green-900">
      {eq.name}
    </h3>

    <p className="text-sm text-gray-500">
      Category: {eq.category}
    </p>

    <p className="mt-2 font-semibold text-green-700">
      ₹ {eq.rentPerDay} / day
    </p>

    <span className="mt-3 inline-block text-xs font-semibold px-3 py-1
      rounded-full bg-green-100 text-green-700 w-fit">
      Available
    </span>

    <button
      onClick={() => handleDelete(eq.id)}
      className="mt-auto bg-red-600 text-white rounded-lg py-2
        text-sm font-semibold hover:bg-red-700 mt-5"
    >
      Delete
    </button>
  </div>
</div>

            ))}
          </div>

          {/* PAGINATION */}
          {totalPages > 1 && (
            <div className="flex items-center justify-center gap-4 mt-10">
              <button
                onClick={() => setCurrentPage(p => p - 1)}
                disabled={currentPage === 1}
                className="px-4 py-2 border rounded-lg bg-white disabled:opacity-50"
              >
                ← Previous
              </button>

              <span className="text-sm font-medium text-gray-700">
                Page {currentPage} of {totalPages}
              </span>

              <button
                onClick={() => setCurrentPage(p => p + 1)}
                disabled={currentPage === totalPages}
                className="px-4 py-2 border rounded-lg bg-white disabled:opacity-50"
              >
                Next →
              </button>
            </div>
          )}
        </>
      )}

      {/* ADD MODAL */}
      {showAddModal && (
        <AddEquipmentModal
          onClose={() => setShowAddModal(false)}
          onSuccess={loadEquipments}
        />
      )}
    </div>
  );
}
