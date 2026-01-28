import { useState } from "react";
import api from "../../services/api";
import { errorToast, successToast, warningToast } from "../../utils/toast";

export default function Settings() {
  const [form, setForm] = useState({
    oldPassword: "",
    newPassword: "",
    confirmPassword: "",
  });

  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const submit = async () => {
    const { oldPassword, newPassword, confirmPassword } = form;

    if (!oldPassword || !newPassword || !confirmPassword) {
      warningToast("Fill all fields");
      return;
    }

    if (newPassword !== confirmPassword) {
      errorToast("Passwords do not match");
      return;
    }

    try {
      setLoading(true);

      // 🔐 AUTHENTICATED CHANGE PASSWORD
      await api.post("/auth/change-password", {
        oldPassword,
        newPassword,
      });

      successToast("Password updated successfully");
      setForm({ oldPassword: "", newPassword: "", confirmPassword: "" });

    } catch (err) {
      errorToast(
        err.response?.data?.message ||
        "Failed to update password"
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-xl bg-white rounded-2xl shadow p-6">
      <h1 className="text-2xl font-bold mb-6">Settings ⚙️</h1>

      <div className="border rounded-xl p-4 space-y-4">
        <h2 className="font-semibold">Change Password</h2>

        <input
          type="password"
          name="oldPassword"
          placeholder="Current password"
          value={form.oldPassword}
          onChange={handleChange}
          className="w-full border px-4 py-2 rounded-lg"
        />

        <input
          type="password"
          name="newPassword"
          placeholder="New password"
          value={form.newPassword}
          onChange={handleChange}
          className="w-full border px-4 py-2 rounded-lg"
        />

        <input
          type="password"
          name="confirmPassword"
          placeholder="Confirm new password"
          value={form.confirmPassword}
          onChange={handleChange}
          className="w-full border px-4 py-2 rounded-lg"
        />

        <button
          onClick={submit}
          disabled={loading}
          className="bg-green-700 text-white px-4 py-2 rounded-lg"
        >
          {loading ? "Updating..." : "Update Password"}
        </button>
      </div>
    </div>
  );
}
