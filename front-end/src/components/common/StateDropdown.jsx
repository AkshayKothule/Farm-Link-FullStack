
import { useEffect, useRef, useState } from "react";
import { INDIA_STATES } from "../../constants/indiaStates";

export default function StateDropdown({ value, onChange }) {
  const [open, setOpen] = useState(false);
  const dropdownRef = useRef(null);

  // Close on outside click
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
        setOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () =>
      document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <div className="relative" ref={dropdownRef}>
      {/* Trigger */}
      <button
        type="button"
        onClick={() => setOpen(!open)}
        className="
          w-full flex items-center justify-between
          border px-4 py-2 rounded-lg bg-white
          text-gray-700
          focus:outline-none focus:ring-2 focus:ring-green-500
          transition
        "
      >
        <span className={value ? "text-gray-800" : "text-gray-400"}>
          {value || "Select State"}
        </span>

        {/* Arrow */}
        <svg
          className={`h-4 w-4 text-gray-400 transition-transform ${
            open ? "rotate-180" : ""
          }`}
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
            d="M19 9l-7 7-7-7"
          />
        </svg>
      </button>

      {/* Dropdown */}
      {open && (
        <div
          className="
            absolute z-30 mt-1 w-full
            bg-white border rounded-lg shadow-lg
            max-h-56 overflow-y-auto
          "
        >
          {INDIA_STATES.map((state) => (
            <div
              key={state}
              onClick={() => {
                onChange(state);
                setOpen(false);
              }}
              className="
                px-4 py-2 text-sm text-gray-700
                hover:bg-green-50 hover:text-green-700
                cursor-pointer
                transition
              "
            >
              {state}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
