import { useState } from "react";
import api from "../../services/api";
import { errorToast, successToast, warningToast } from "../../utils/toast";

export default function PasswordResetRequestModal({ onClose }) {
  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);

  const submit = async () => {
    if (!email) {
      warningToast("Enter registered email");
      return;
    }

    try {
      setLoading(true);

      await api.post("/auth/forgot-password", { email });

      successToast("📧 Reset password email sent. Check your inbox.");
      onClose(); // ✅ modal बंद

    } catch (err) {
      errorToast(
        err.response?.data?.message ||
        "Failed to send reset email"
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center">
      <div className="bg-white p-6 rounded-xl w-full max-w-md">

        <h2 className="text-lg font-bold mb-4">
          Reset Password
        </h2>

        <input
          type="email"
          placeholder="Enter registered email"
          value={email}
          onChange={e => setEmail(e.target.value)}
          className="w-full border px-3 py-2 rounded mb-4"
        />

        <div className="flex justify-end gap-2">
          <button
            onClick={onClose}
            className="px-4 py-2 rounded border"
          >
            Cancel
          </button>

          <button
            onClick={submit}
            disabled={loading}
            className="bg-green-700 text-white px-4 py-2 rounded"
          >
            {loading ? "Sending..." : "Send Reset Link"}
          </button>
        </div>

      </div>
    </div>
  );
}
