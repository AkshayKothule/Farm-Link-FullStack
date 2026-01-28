import { Link } from "react-router-dom";
import Navbar from "../components/layout/Navbar";

// Images
import heroImg from "../assets/Images/hero.avif";
import aboutImg from "../assets/Images/lady.avif";
import service1 from "../assets/Images/tractor.avif";
import service2 from "../assets/Images/small_tractor.avif";
import service3 from "../assets/Images/men_women.avif";
import farmer1 from "../assets/Images/farmer.avif";
import farmer2 from "../assets/Images/farmer2.avif";
import farmer3 from "../assets/Images/lady2.avif";

export default function LandingPage() {
  return (
    <div className="bg-white text-gray-800 overflow-x-hidden">
      <Navbar />

      {/* ================= HERO ================= */}
      <section id="home" className="relative h-[90vh] md:h-[95vh]">
        <img
          src={heroImg}
          className="absolute inset-0 w-full h-full object-cover"
          alt="Organic Farming"
        />

        <div className="absolute inset-0 bg-gradient-to-r from-green-950/90 via-green-900/70 to-transparent" />

        <div className="relative z-10 max-w-7xl mx-auto px-6 h-full flex items-center text-white">
          <div className="max-w-3xl animate-fade-in">

            <span
              className="
                inline-flex items-center justify-center
                mb-6 px-4 sm:px-5 py-2
                rounded-full border border-yellow-400
                text-yellow-300 text-xs sm:text-sm
                font-semibold text-center
                max-w-full break-words
              "
            >
              We are Producing Natural Products
            </span>

            <h1 className="text-4xl sm:text-5xl md:text-6xl font-extrabold leading-tight">
            Powering Farms with Modern Equipment            </h1>

            <p className="mt-6 text-lg text-gray-200 leading-relaxed">
              Rent modern farming equipment when you need it — affordable, easy, and reliable.
            </p>

            <div className="mt-10 flex flex-wrap gap-4">
              <button
                onClick={() =>
                  document.getElementById("services")?.scrollIntoView({
                    behavior: "smooth",
                  })
                }
                className="bg-green-600 px-8 py-4 rounded-full font-semibold transition hover:scale-105 hover:bg-green-700"
              >
           Explore Equipment →           
              </button>

              <Link
                to="/auth"
                className="border border-yellow-400 text-yellow-300 px-8 py-4 rounded-full transition hover:bg-yellow-400 hover:text-black"
              >
          Start Renting Now →            
         </Link>
            </div>
          </div>
        </div>
      </section>

      {/* ================= ABOUT ================= */}
      <section id="about" className="py-20 bg-white">
        <div className="max-w-6xl mx-auto px-6 grid md:grid-cols-2 gap-12 items-center">
          <img
            src={aboutImg}
            alt="Healthy Farming"
            className="rounded-3xl shadow mx-auto transition hover:scale-105"
          />

          <div className="animate-fade-in">
            <h2 className="text-3xl font-bold mb-4">
            Smart Farming for a Better Tomorrow            </h2>
            <p className="text-lg text-gray-600 leading-relaxed">
              FarmLink connects farmers and equipment owners on one platform, enabling affordable access to modern farming tools and supporting smarter, more sustainable agriculture.
            </p>
          </div>
        </div>
      </section>

      {/* ================= SERVICES ================= */}
      <section id="services" className="bg-green-50 py-20">
        <h2 className="text-3xl font-bold text-center mb-14 animate-fade-in">
          What We’re Offering
        </h2>

        <div className="max-w-6xl mx-auto px-6 grid md:grid-cols-3 gap-8">
          {[service1, service2, service3].map((img, i) => (
            <div
              key={i}
              className="bg-white rounded-3xl overflow-hidden shadow
                         transition hover:shadow-xl hover:-translate-y-1"
            >
              <img
                src={img}
                alt="Service"
                className="h-56 w-full object-cover transition hover:scale-105"
              />
              <div className="p-6">
                <h3 className="text-xl font-semibold mb-2">
                  Agriculture Service
                </h3>
                <p className="text-gray-600 text-sm">
                  Modern equipment & eco-friendly solutions.
                </p>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* ================= WHY CHOOSE US ================= */}
      <section id="shop" className="py-20 bg-white">
        <div className="max-w-6xl mx-auto px-6 grid md:grid-cols-2 gap-12 items-center">
          <div className="animate-fade-in">
            <h2 className="text-3xl font-bold mb-6">
              Why Choose FarmLink
            </h2>
            <ul className="space-y-4 text-lg text-gray-600">
              <li>✔ Sustainable farming</li>
              <li>✔ Affordable equipment rental</li>
              <li>✔ Trusted farmer network</li>
              <li>✔ Fast & simple booking</li>
            </ul>
          </div>

          <img
            src={service2}
            alt="Equipment"
            className="rounded-3xl shadow mx-auto transition hover:scale-105"
          />
        </div>
      </section>

      {/* ================= FARMERS ================= */}
      <section className="bg-green-100 py-20">
        <h2 className="text-3xl font-bold text-center mb-14 animate-fade-in">
          Our Happy Farmers
        </h2>

        <div className="max-w-6xl mx-auto px-6 grid md:grid-cols-3 gap-8">
          {[farmer1, farmer2, farmer3].map((img, i) => (
            <div
              key={i}
              className="bg-white rounded-3xl shadow overflow-hidden
                         transition hover:shadow-xl hover:-translate-y-1"
            >
              <img
                src={img}
                alt="Farmer"
                className="h-64 w-full object-cover transition hover:scale-105"
              />
              <div className="p-4 text-center font-semibold">
                Organic Farmer
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* ================= CTA ================= */}
      <section className="bg-green-700 text-white py-20 text-center">
        <h2 className="text-3xl md:text-4xl font-bold mb-6 animate-fade-in">
          Ready to Grow Naturally?
        </h2>
        <p className="text-lg mb-8">
          Join FarmLink and experience sustainable farming.
        </p>

        <Link
          to="/auth"
          className="bg-yellow-400 text-black px-10 py-4 rounded-full font-bold transition hover:scale-105 hover:bg-yellow-300"
        >
          Get Started Now →
        </Link>
      </section>

      {/* ================= FOOTER ================= */}
      <footer className="bg-gray-900 text-gray-400 py-6 text-center text-sm">
        © 2026 FarmLink • Inspired by Eco Agriculture 🌱
      </footer>
    </div>
  );
}
