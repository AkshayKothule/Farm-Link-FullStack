import { useEffect, useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import api from "../../services/api";
import { addDays, isWithinInterval } from "date-fns";
import { successToast } from "../../utils/toast";

export default function RentalRequestModal({ equipment, onClose }) {
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [bookedRanges, setBookedRanges] = useState([]);
  const [loading, setLoading] = useState(false);

  // fetch booked ranges
  useEffect(() => {
    api
      .get(`/farmers/equipments/${equipment.id}/booked-dates`)
      .then(res => {
        setBookedRanges(res.data || []);
      });
  }, [equipment.id]);

  // tomorrow minimum
  const minDate = addDays(new Date(), 1);

  // disable booked dates in calendar
  const isDateBooked = (date) => {
    return bookedRanges.some(r =>
      isWithinInterval(date, {
        start: new Date(r.startDate),
        end: new Date(r.endDate),
      })
    );
  };

  const submit = () => {
    if (!startDate || !endDate) return;

    setLoading(true);
    api.post("/rentals/farmer", {
      equipmentId: equipment.id,
      startDate: startDate.toISOString().split("T")[0],
      endDate: endDate.toISOString().split("T")[0],
    })
    .then(() => {
      successToast("✅ Rental request submitted");
      onClose();
    })
    .finally(() => setLoading(false));
  };

  return (
    <>
      <div className="fixed inset-0 bg-black/40 z-40" onClick={onClose} />

      <div className="fixed inset-0 flex items-center justify-center z-50">
        <div className="bg-white rounded-xl p-6 w-full max-w-md">

          <h2 className="text-xl font-bold mb-4">
            Request Rental 🚜
          </h2>

          {/* START DATE */}
          <label className="text-sm font-medium">Start Date</label>
          <DatePicker
            selected={startDate}
            onChange={(date) => {
              setStartDate(date);
              setEndDate(null);
            }}
            minDate={minDate}
            filterDate={(date) => !isDateBooked(date)}
            placeholderText="Select start date"
            className="w-full border p-2 rounded mt-1"
          />

          {/* END DATE */}
          <label className="text-sm font-medium mt-4 block">End Date</label>
          <DatePicker
            selected={endDate}
            onChange={setEndDate}
            minDate={startDate || minDate}
            filterDate={(date) => !isDateBooked(date)}
            disabled={!startDate}
            placeholderText="Select end date"
            className="w-full border p-2 rounded mt-1 disabled:opacity-50"
          />

          <div className="flex gap-3 mt-6">
            <button
              onClick={onClose}
              className="w-1/2 border py-2 rounded"
            >
              Cancel
            </button>

            <button
              onClick={submit}
              disabled={!startDate || !endDate || loading}
              className="w-1/2 bg-green-600 text-white py-2 rounded disabled:opacity-50"
            >
              {loading ? "Requesting..." : "Request"}
            </button>
          </div>

          <p className="text-xs text-gray-500 mt-3 text-center">
            ℹ Booked dates are disabled in calendar
          </p>
        </div>
      </div>
    </>
  );
}
