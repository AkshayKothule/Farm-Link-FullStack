import { useState } from "react";
import api from "../../services/api";
import { errorToast } from "../../utils/toast";

export default function EditFarmerProfileModal({ profile, onClose, onSuccess }) {
  const [form, setForm] = useState({
    firstName: profile.firstName || "",
    lastName: profile.lastName || "",
    phoneNumber: profile.phoneNumber || "",
    farmType: profile.farmType || "",
    landArea: profile.landArea || "",
  });

  const [saving, setSaving] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setSaving(true);

    api
      .put("/farmers/profile", {
        ...form,
        landArea: Number(form.landArea),
      })
      .then(() => onSuccess())
      .catch(() => errorToast("Failed to update profile"))
      .finally(() => setSaving(false));
  };

  return (
    <>
      {/* BLUR BACKDROP */}
      <div
        className="fixed inset-0 bg-black/40 backdrop-blur-sm z-40"
        onClick={onClose}
      />

      {/* MODAL */}
      <div className="fixed inset-0 z-50 flex items-center justify-center">
        <form
          onSubmit={handleSubmit}
          className="bg-white rounded-2xl shadow-xl w-full max-w-lg p-6 space-y-5"
        >
          <h2 className="text-xl font-bold text-green-900">
            Edit Profile ✏️
          </h2>

          <div className="grid grid-cols-2 gap-4">
            <input
              name="firstName"
              value={form.firstName}
              onChange={handleChange}
              placeholder="First Name"
              required
              className="border px-4 py-2 rounded-lg"
            />
            <input
              name="lastName"
              value={form.lastName}
              onChange={handleChange}
              placeholder="Last Name"
              required
              className="border px-4 py-2 rounded-lg"
            />
          </div>

          <input
            name="phoneNumber"
            value={form.phoneNumber}
            onChange={handleChange}
            placeholder="Phone Number"
            required
            className="w-full border px-4 py-2 rounded-lg"
          />

          <input
            name="farmType"
            value={form.farmType}
            onChange={handleChange}
            placeholder="Farm Type"
            required
            className="w-full border px-4 py-2 rounded-lg"
          />

          <input
            type="number"
            name="landArea"
            value={form.landArea}
            onChange={handleChange}
            placeholder="Land Area (Acres)"
            required
            className="w-full border px-4 py-2 rounded-lg"
          />

          <div className="flex justify-end gap-3 pt-4">
            <button
              type="button"
              onClick={onClose}
              className="px-5 py-2 rounded-lg border font-semibold"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={saving}
              className="bg-green-600 text-white px-6 py-2 rounded-lg font-semibold hover:bg-green-700"
            >
              {saving ? "Saving..." : "Save"}
            </button>
          </div>
        </form>
      </div>
    </>
  );
}
