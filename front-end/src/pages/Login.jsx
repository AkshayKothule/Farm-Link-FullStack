import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { loginSuccess } from "../redux/slices/authSlice";
import api from "../services/api";
import PasswordResetRequestModal from "../components/modals/PasswordResetRequestModal";
import { errorToast, successToast, warningToast } from "../utils/toast";

export default function Login() {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const [form, setForm] = useState({
    email: "",
    password: "",
  });

  const [showPassword, setShowPassword] = useState(false);
  const [showResetModal, setShowResetModal] = useState(false);

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const res = await api.post("/auth/login", form);
      const { token, role } = res.data;

      dispatch(loginSuccess({ token, role }));
      successToast("Login Succesfully!...");
      if (role === "FARMER" || role === "ROLE_FARMER") {
        navigate("/farmer");
      } else if (role === "OWNER" || role === "ROLE_OWNER") {
        navigate("/owner");
      } else if (role === "ADMIN" || role === "ROLE_ADMIN") {
        navigate("/admin");
      } else {
        warningToast("Unknown role");
      }
    } catch (err) {
      errorToast(
        err.response?.data?.message ||
          "Login failed. Please check credentials.",
      );
    }
  };

  return (
    <>
      <form onSubmit={handleLogin} className="space-y-4">
        {/* Email */}
        <input
          type="email"
          name="email"
          placeholder="Email"
          required
          onChange={handleChange}
          className="w-full border px-4 py-2 rounded-lg"
        />

        {/* Password */}
        <div className="relative">
          <input
            type={showPassword ? "text" : "password"}
            name="password"
            placeholder="Password"
            required
            onChange={handleChange}
            className="w-full border px-4 py-2 pr-12 rounded-lg"
          />

          <button
            type="button"
            onClick={() => setShowPassword(!showPassword)}
            className="absolute inset-y-0 right-3 flex items-center text-gray-500"
          >
            {showPassword ? "🙈" : "👁️"}
          </button>
        </div>

        {/* 🔑 Forgot Password */}
        <div className="text-right">
          <button
            type="button"
            onClick={() => setShowResetModal(true)}
            className="text-sm text-green-700 hover:underline"
          >
            Forgot password?
          </button>
        </div>

        {/* Submit */}
        <button
          type="submit"
          className="w-full bg-green-600 text-white py-2 rounded-lg font-semibold hover:bg-green-700 transition"
        >
          Login
        </button>
      </form>

      {/* 📧 RESET PASSWORD MODAL */}
      {showResetModal && (
        <PasswordResetRequestModal onClose={() => setShowResetModal(false)} />
      )}
    </>
  );
}
