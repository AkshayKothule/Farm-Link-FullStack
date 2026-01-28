import { useState, useRef } from "react";
import ImagePreviewModal from "../modals/ImagePreviewModal";

const PLACEHOLDER =
  "https://via.placeholder.com/600x400?text=No+Image";

export default function ImageCarousel({ images = [] }) {
  const [index, setIndex] = useState(0);
  const [open, setOpen] = useState(false);
  const touchStartX = useRef(null);

  const imgs = images.length > 0 ? images : [PLACEHOLDER];

  const next = () =>
    setIndex((i) => (i + 1) % imgs.length);

  const prev = () =>
    setIndex((i) => (i === 0 ? imgs.length - 1 : i - 1));

  // 📱 SWIPE HANDLERS
  const onTouchStart = (e) => {
    touchStartX.current = e.touches[0].clientX;
  };

  const onTouchEnd = (e) => {
    if (!touchStartX.current) return;
    const diff =
      touchStartX.current - e.changedTouches[0].clientX;

    if (diff > 50) next();
    if (diff < -50) prev();

    touchStartX.current = null;
  };

  return (
    <>
      <div
        className="relative group overflow-hidden rounded-xl bg-gray-100"
        onTouchStart={onTouchStart}
        onTouchEnd={onTouchEnd}
      >
        {/* IMAGE */}
        <div className="aspect-[4/3] sm:aspect-[16/9]">
          <img
            src={imgs[index]}
            alt="equipment"
            loading="lazy"        // ⚡ LAZY LOAD
            onClick={() => setOpen(true)}
            className="
              w-full h-full object-cover cursor-pointer
              transition-transform duration-500
            "
          />
        </div>

        {/* ARROWS (DESKTOP) */}
        {imgs.length > 1 && (
          <>
            <button
              onClick={prev}
              className="
                absolute top-1/2 left-3 -translate-y-1/2
                bg-black/60 hover:bg-black/80 text-white
                rounded-full p-2 hidden sm:flex
                opacity-0 group-hover:opacity-100 transition
              "
            >
              ‹
            </button>

            <button
              onClick={next}
              className="
                absolute top-1/2 right-3 -translate-y-1/2
                bg-black/60 hover:bg-black/80 text-white
                rounded-full p-2 hidden sm:flex
                opacity-0 group-hover:opacity-100 transition
              "
            >
              ›
            </button>
          </>
        )}

        {/* DOTS */}
        {imgs.length > 1 && (
          <div className="absolute bottom-3 left-1/2 -translate-x-1/2 flex gap-2">
            {imgs.map((_, i) => (
              <span
                key={i}
                onClick={() => setIndex(i)}
                className={`
                  h-2.5 w-2.5 rounded-full cursor-pointer
                  transition
                  ${i === index
                    ? "bg-white scale-110"
                    : "bg-white/50 hover:bg-white"}
                `}
              />
            ))}
          </div>
        )}
      </div>

      {/* FULLSCREEN VIEW */}
      {open && (
        <ImagePreviewModal
          images={imgs}
          index={index}
          onClose={() => setOpen(false)}
        />
      )}
    </>
  );
}
