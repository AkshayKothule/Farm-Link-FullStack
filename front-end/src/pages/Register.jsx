import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import StateDropdown from "../components/common/StateDropdown";
import { errorToast, successToast } from "../utils/toast";

export default function Register() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phoneNumber: "",
    password: "",
    role: "FARMER",
    addressDto: {
      addressLine: "",
      village: "",
      taluka: "",
      district: "",
      state: "",
      pincode: "",
    },
  });

  const [showPassword, setShowPassword] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name in form.addressDto) {
      setForm({
        ...form,
        addressDto: {
          ...form.addressDto,
          [name]: value,
        },
      });
    } else {
      setForm({
        ...form,
        [name]: value,
      });
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      await api.post("/auth/register", form);
      successToast("Registration successful! Please login.");
      navigate("/auth");
    } catch (err) {
      errorToast(
        err.response?.data?.message ||
          "Registration failed. Please check details."
      );
    }
  };

  return (
    <form onSubmit={handleRegister} className="space-y-4">

      {/* ===== BASIC DETAILS ===== */}
      <div className="flex gap-3">
        <input
          name="firstName"
          placeholder="First Name"
          required
          onChange={handleChange}
          className="w-1/2 border px-4 py-2 rounded-lg"
        />
        <input
          name="lastName"
          placeholder="Last Name"
          required
          onChange={handleChange}
          className="w-1/2 border px-4 py-2 rounded-lg"
        />
      </div>

      <input
        type="email"
        name="email"
        placeholder="Email"
        required
        onChange={handleChange}
        className="w-full border px-4 py-2 rounded-lg"
      />

      <input
        type="text"
        name="phoneNumber"
        placeholder="Phone Number"
        required
        onChange={handleChange}
        className="w-full border px-4 py-2 rounded-lg"
      />

      {/* ===== PASSWORD (INDUSTRY STANDARD) ===== */}
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
          className="absolute inset-y-0 right-3 flex items-center text-gray-500 hover:text-gray-700"
          aria-label={showPassword ? "Hide password" : "Show password"}
        >
          {showPassword ? (
            /* Eye Off */
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-5 w-5"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M13.875 18.825A10.05 10.05 0 0112 19c-5.523 0-10-4.477-10-10 0-1.118.183-2.193.52-3.197M6.223 6.223A9.955 9.955 0 0112 5c5.523 0 10 4.477 10 10 0 1.437-.304 2.803-.85 4.04M15 12a3 3 0 11-6 0 3 3 0 016 0z"
              />
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M3 3l18 18"
              />
            </svg>
          ) : (
            /* Eye */
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-5 w-5"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
              />
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
              />
            </svg>
          )}
        </button>
      </div>

      <select
        name="role"
        onChange={handleChange}
        className="w-full border px-4 py-2 rounded-lg bg-white"
      >
        <option value="FARMER">Farmer</option>
        <option value="OWNER">Equipment Owner</option>
      </select>

      {/* ===== ADDRESS DETAILS ===== */}
      <h3 className="font-semibold text-gray-600 pt-4">
        Address Details
      </h3>

      <input
        name="addressLine"
        placeholder="Address Line"
        required
        onChange={handleChange}
        className="w-full border px-4 py-2 rounded-lg"
      />

      <input
        name="village"
        placeholder="Village"
        required
        onChange={handleChange}
        className="w-full border px-4 py-2 rounded-lg"
      />

      <input
        name="taluka"
        placeholder="Taluka"
        required
        onChange={handleChange}
        className="w-full border px-4 py-2 rounded-lg"
      />

      <input
        name="district"
        placeholder="District"
        required
        onChange={handleChange}
        className="w-full border px-4 py-2 rounded-lg"
      />

      {/* ===== STATE DROPDOWN ===== */}
   <StateDropdown
  value={form.addressDto.state}
  onChange={(state) =>
    setForm({
      ...form,
      addressDto: {
        ...form.addressDto,
        state,
      },
    })
  }
/>



      <input
        name="pincode"
        placeholder="Pincode"
        required
        onChange={handleChange}
        className="w-full border px-4 py-2 rounded-lg"
      />

      {/* ===== SUBMIT ===== */}
      <button
        type="submit"
        className="w-full bg-green-600 text-white py-2 rounded-lg font-semibold hover:bg-green-700 transition"
      >
        Register
      </button>
    </form>
  );
}
