import { useState } from "react";
import RentalRequestModal from "../modals/RentalRequestModal";
import ImageCarousel from "../common/ImageCarousel";
import ImagePreviewModal from "../modals/ImagePreviewModal";

export default function EquipmentCard({ equipment }) {
  const [open, setOpen] = useState(false);
  const [preview, setPreview] = useState(null);

  const images = equipment.imageUrls || [];

  return (
    <>
      <div className="bg-white rounded-2xl shadow hover:shadow-lg transition p-5 flex flex-col">

        {/* ===== IMAGE CAROUSEL ===== */}
        <ImageCarousel
          images={images}
          onClick={() =>
            images.length > 0 && setPreview(images[0])
          }
        />

        {/* ===== INFO ===== */}
        <h3 className="text-lg font-semibold text-green-900 mt-3">
          {equipment.name}
        </h3>

        <p className="text-xs text-gray-500">
          Category: {equipment.category}
        </p>

        <p className="text-sm text-gray-600 line-clamp-2 mt-1">
          {equipment.description || "No description available"}
        </p>

        <div className="text-sm text-gray-700 mt-2">
          <span className="font-medium">Business:</span>{" "}
          {equipment.ownerBusinessName || "Verified Owner"}
        </div>

        <div className="flex items-center justify-between mt-3">
          <div className="text-green-700 font-bold">
            ₹ {equipment.rentPerDay} / day
          </div>

          <span className="text-xs bg-green-100 text-green-700 px-2 py-1 rounded-full font-semibold">
            Available
          </span>
        </div>

        {/* ===== ACTION ===== */}
        <button
          onClick={() => setOpen(true)}
          className="mt-auto bg-green-600 text-white py-2 rounded-lg
            font-semibold hover:bg-green-700 transition mt-4"
        >
          Request Rental
        </button>
      </div>

      {/* RENTAL MODAL */}
      {open && (
        <RentalRequestModal
          equipment={equipment}
          onClose={() => setOpen(false)}
        />
      )}

      {/* IMAGE PREVIEW MODAL */}
      {preview && (
        <ImagePreviewModal
          image={preview}
          onClose={() => setPreview(null)}
        />
      )}
    </>
  );
}
