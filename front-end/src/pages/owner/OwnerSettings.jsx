import { useState } from "react";
import api from "../../services/api";
import { errorToast, successToast, warningToast } from "../../utils/toast";

export default function OwnerSettings() {
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [loading, setLoading] = useState(false);

  const submit = async () => {
    if (!currentPassword || !newPassword || !confirm) {
      warningToast("Fill all fields");
      return;
    }

    if (newPassword !== confirm) {
      warningToast("Passwords do not match");
      return;
    }

    try {
      setLoading(true);

      // ✅ FIX 1: POST (not PUT)
      // ✅ FIX 2: oldPassword key (not currentPassword)
      await api.post("/auth/change-password", {
        oldPassword: currentPassword,
        newPassword: newPassword,
      });

      successToast("✅ Password updated successfully");

      setCurrentPassword("");
      setNewPassword("");
      setConfirm("");
    } catch (err) {
      errorToast(err.response?.data || "Update failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-xl bg-white rounded-2xl shadow p-6">
      <h1 className="text-2xl font-bold mb-6">
        Settings ⚙️
      </h1>

      <h2 className="font-semibold mb-4">
        Change Password
      </h2>

      <input
        type="password"
        placeholder="Current password"
        value={currentPassword}
        onChange={e => setCurrentPassword(e.target.value)}
        className="w-full border p-2 rounded mb-3"
      />

      <input
        type="password"
        placeholder="New password"
        value={newPassword}
        onChange={e => setNewPassword(e.target.value)}
        className="w-full border p-2 rounded mb-3"
      />

      <input
        type="password"
        placeholder="Confirm new password"
        value={confirm}
        onChange={e => setConfirm(e.target.value)}
        className="w-full border p-2 rounded mb-4"
      />

      <button
        onClick={submit}
        disabled={loading}
        className="bg-green-700 text-white px-6 py-2 rounded-lg"
      >
        {loading ? "Updating..." : "Update Password"}
      </button>
    </div>
  );
}
