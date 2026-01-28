import { useState } from "react";
import { useLocation } from "react-router-dom";
import Login from "../pages/Login";
import Register from "../pages/Register";


export default function AuthPage() {
  const [mode, setMode] = useState("login");
  const location = useLocation();

  // 🔥 reset-password page वर AuthPageच render करू नको
  if (location.pathname === "/reset-password") {
    return null;
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-green-50 px-4">
      <div className="bg-white w-full max-w-md p-8 rounded-3xl shadow">

        {/* Toggle */}
        <div className="flex justify-between mb-6">
          <button
            onClick={() => setMode("login")}
            className={`font-semibold ${
              mode === "login"
                ? "text-green-600 border-b-2 border-green-600"
                : "text-gray-400"
            }`}
          >
            Login
          </button>

          <button
            onClick={() => setMode("register")}
            className={`font-semibold ${
              mode === "register"
                ? "text-green-600 border-b-2 border-green-600"
                : "text-gray-400"
            }`}
          >
            Register
          </button>
        </div>

        {mode === "login" ? <Login /> : <Register />}
      </div>
    </div>
  );
}
