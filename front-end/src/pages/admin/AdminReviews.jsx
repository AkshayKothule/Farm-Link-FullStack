import { useEffect, useState } from "react";
import { getReviews, deleteReview } from "../../services/adminService";
import { successToast, errorToast } from "../../utils/toast";

export default function AdminReviews() {
  const [reviews, setReviews] = useState([]);

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    try {
      const res = await getReviews();
      setReviews(res.data || []);
    } catch {
      errorToast("Failed to load reviews");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this review?")) return;

    try {
      await deleteReview(id);
      successToast("Review deleted");
      load();
    } catch {
      errorToast("Delete failed");
    }
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Reviews 📝</h1>

      <div className="space-y-4">

        {reviews.length === 0 ? (
  <p className="text-gray-500">
    No reviews found
  </p>
) : (
  reviews.map(r => (
          <div
            key={r.id}
            className="bg-white rounded-xl shadow p-4 flex justify-between"
          >
            <div>
              <p className="font-semibold">{r.userName}</p>
              <p className="text-sm text-gray-600">{r.comment}</p>
            </div>

            <button
              onClick={() => handleDelete(r.id)}
              className="bg-red-600 text-white px-3 py-1 rounded"
            >
              Delete
            </button>
          </div>
        ))
)}

    
      </div>
    </div>
  );
}
