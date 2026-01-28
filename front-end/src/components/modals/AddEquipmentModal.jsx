import { useState } from "react";
import api from "../../services/api";
import { errorToast, successToast } from "../../utils/toast";

export default function AddEquipmentModal({ onClose, onSuccess }) {
  const [form, setForm] = useState({
    name: "",
    category: "",
    rentPerDay: "",
    description: "",
  });

  const [images, setImages] = useState([]);
  const [activeIndex, setActiveIndex] = useState(0);
  const [loading, setLoading] = useState(false);

  // ================= FORM CHANGE =================
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // ================= IMAGE CHANGE =================
  const handleImageChange = (e) => {
    const files = Array.from(e.target.files);
    const combined = [...images, ...files];

    if (combined.length > 5) {
      errorToast("Maximum 5 images allowed");
      return;
    }

    for (let file of files) {
      if (!file.type.startsWith("image/")) {
        errorToast("Only image files allowed");
        return;
      }
    }

    setImages(combined);
    e.target.value = ""; // allow reselect
  };

  // ================= REMOVE IMAGE =================
  const removeImage = (index) => {
    const updated = images.filter((_, i) => i !== index);
    setImages(updated);

    if (activeIndex >= updated.length) {
      setActiveIndex(Math.max(0, updated.length - 1));
    }
  };

  // ================= CAROUSEL =================
  const prev = () => {
    setActiveIndex(i => (i === 0 ? images.length - 1 : i - 1));
  };

  const next = () => {
    setActiveIndex(i => (i === images.length - 1 ? 0 : i + 1));
  };

  // ================= SUBMIT =================
  const handleSubmit = async () => {
    if (!form.name || !form.category || !form.rentPerDay) {
      errorToast("Please fill all required fields");
      return;
    }

    try {
      setLoading(true);

      const formData = new FormData();

      formData.append(
        "data",
        new Blob(
          [
            JSON.stringify({
              ...form,
              rentPerDay: Number(form.rentPerDay),
            }),
          ],
          { type: "application/json" }
        )
      );

      images.forEach((img) => {
        formData.append("images", img);
      });

      await api.post("/owners/equipments", formData);

      successToast("🚜 Equipment added successfully");

      setForm({
        name: "",
        category: "",
        rentPerDay: "",
        description: "",
      });
      setImages([]);
      setActiveIndex(0);

      onSuccess();
      onClose();
    } catch (err) {
      errorToast(err.response?.data?.message || "Failed to add equipment");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      {/* BACKDROP */}
      <div
        className="fixed inset-0 bg-black/40 backdrop-blur-sm z-40"
        onClick={onClose}
      />

      {/* MODAL */}
      <div className="fixed inset-0 flex items-center justify-center z-50">
        <div className="bg-white rounded-2xl shadow-xl w-full max-w-md p-6">

          <h2 className="text-xl font-bold mb-4">
            Add New Equipment 🚜
          </h2>

          <div className="space-y-3">

            {/* FORM */}
            <input
              name="name"
              placeholder="Equipment Name"
              value={form.name}
              onChange={handleChange}
              className="w-full border px-4 py-2 rounded-lg"
            />

            <input
              name="category"
              placeholder="Category"
              value={form.category}
              onChange={handleChange}
              className="w-full border px-4 py-2 rounded-lg"
            />

            <input
              name="rentPerDay"
              type="number"
              placeholder="Rent per day"
              value={form.rentPerDay}
              onChange={handleChange}
              className="w-full border px-4 py-2 rounded-lg"
            />

            <textarea
              name="description"
              placeholder="Description (optional)"
              value={form.description}
              onChange={handleChange}
              className="w-full border px-4 py-2 rounded-lg"
            />

            {/* IMAGE INPUT */}
            <input
              type="file"
              multiple
              accept="image/*"
              onChange={handleImageChange}
              className="w-full text-sm"
            />

            {/* 🖼 IMAGE CAROUSEL PREVIEW */}
            {images.length > 0 && (
              <div className="relative mt-3">

                <img
                  src={URL.createObjectURL(images[activeIndex])}
                  className="w-full h-48 object-cover rounded-xl"
                  alt="preview"
                />

                {/* ARROWS */}
                {images.length > 1 && (
                  <>
                    <button
                      onClick={prev}
                      className="absolute left-2 top-1/2 -translate-y-1/2 bg-black/40 text-white rounded-full px-2"
                    >
                      ‹
                    </button>

                    <button
                      onClick={next}
                      className="absolute right-2 top-1/2 -translate-y-1/2 bg-black/40 text-white rounded-full px-2"
                    >
                      ›
                    </button>
                  </>
                )}

                {/* REMOVE */}
                <button
                  onClick={() => removeImage(activeIndex)}
                  className="absolute top-2 right-2 bg-red-600 text-white rounded-full px-2 text-xs"
                >
                  ✕
                </button>

                {/* DOTS */}
                <div className="flex justify-center gap-1 mt-2">
                  {images.map((_, i) => (
                    <span
                      key={i}
                      onClick={() => setActiveIndex(i)}
                      className={`h-2 w-2 rounded-full cursor-pointer ${
                        i === activeIndex ? "bg-green-700" : "bg-gray-300"
                      }`}
                    />
                  ))}
                </div>
              </div>
            )}
          </div>

          {/* ACTIONS */}
          <div className="flex gap-3 mt-6">
            <button
              onClick={onClose}
              disabled={loading}
              className="w-1/2 border rounded-lg py-2"
            >
              Cancel
            </button>

            <button
              onClick={handleSubmit}
              disabled={loading}
              className="w-1/2 bg-green-700 text-white rounded-lg py-2 disabled:opacity-60"
            >
              {loading ? "Saving..." : "Add"}
            </button>
          </div>
        </div>
      </div>
    </>
  );
}
