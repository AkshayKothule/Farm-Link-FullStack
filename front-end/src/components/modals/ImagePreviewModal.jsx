import { useState } from "react";

export default function ImagePreviewModal({
  images,
  index,
  onClose,
}) {
  const [current, setCurrent] = useState(index);

  const next = () =>
    setCurrent((i) => (i + 1) % images.length);

  const prev = () =>
    setCurrent((i) =>
      i === 0 ? images.length - 1 : i - 1
    );

  return (
    <div className="fixed inset-0 z-50 bg-black/90 flex items-center justify-center">

      {/* CLOSE */}
      <button
        onClick={onClose}
        className="absolute top-4 right-6 text-white text-3xl"
      >
        ✕
      </button>

      {/* IMAGE */}
      <img
        src={images[current]}
        alt="preview"
        className="max-h-[90vh] max-w-[90vw] object-contain"
      />

      {/* ARROWS */}
      {images.length > 1 && (
        <>
          <button
            onClick={prev}
            className="absolute left-6 text-white text-4xl"
          >
            ‹
          </button>

          <button
            onClick={next}
            className="absolute right-6 text-white text-4xl"
          >
            ›
          </button>
        </>
      )}
    </div>
  );
}
