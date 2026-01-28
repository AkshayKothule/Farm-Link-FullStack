import { useState, useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import api from "../services/api";
import { errorToast, successToast, warningToast } from "../utils/toast";

export default function ResetPassword() {
  const [params] = useSearchParams();
  const token = params.get("token");
  const navigate = useNavigate();

  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    console.log("🔑 RESET TOKEN FROM URL =", token);

    if (!token) {
      warningToast("Invalid or missing reset token");
      navigate("/auth", { replace: true });
    }
  }, [token, navigate]);

  const submit = async () => {
    if (!password || !confirm) {
      warningToast("Fill all fields");
      return;
    }

    if (password !== confirm) {
      errorToast("Passwords do not match");
      return;
    }

    try {
      setLoading(true);

      await api.post("/auth/reset-password", {
        token,
        newPassword: password,
      });

      successToast("Password reset successful. Please login.");
      navigate("/auth", { replace: true });

    } catch (err) {
      errorToast(
        err.response?.data?.message ||
        "Reset failed. Token may be expired."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-green-50 px-4">
      <div className="bg-white p-6 rounded-2xl shadow w-full max-w-md">

        <h2 className="text-xl font-bold mb-4">
          Set New Password 🔐
        </h2>

        <input
          type={showPassword ? "text" : "password"}
          placeholder="New password"
          value={password}
          onChange={e => setPassword(e.target.value)}
          className="w-full border px-4 py-2 rounded mb-3"
        />

        <input
          type={showPassword ? "text" : "password"}
          placeholder="Confirm password"
          value={confirm}
          onChange={e => setConfirm(e.target.value)}
          className="w-full border px-4 py-2 rounded mb-4"
        />

        <label className="flex items-center gap-2 text-sm mb-4">
          <input
            type="checkbox"
            checked={showPassword}
            onChange={() => setShowPassword(!showPassword)}
          />
          Show password
        </label>

        <button
          onClick={submit}
          disabled={loading}
          className="w-full bg-green-700 text-white py-2 rounded-lg font-semibold"
        >
          {loading ? "Updating..." : "Reset Password"}
        </button>

      </div>
    </div>
  );
}
