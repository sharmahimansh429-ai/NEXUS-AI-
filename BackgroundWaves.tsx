import { useEffect, useRef } from "react";

export default function BackgroundWaves() {
  const canvasRef = useRef<HTMLCanvasElement>(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas?.getContext("2d");
    if (!canvas || !ctx) return;

    let width = 0;
    let height = 0;
    let frame = 0;
    let animationId = 0;
    let lastTime = 0;
    let dots: { x: number; y: number; phase: number; row: number }[] = [];

    const resize = () => {
      const dpr = Math.min(window.devicePixelRatio || 1, 1.5);
      width = window.innerWidth;
      height = window.innerHeight;

      canvas.width = Math.round(width * dpr);
      canvas.height = Math.round(height * dpr);
      canvas.style.width = `${width}px`;
      canvas.style.height = `${height}px`;
      ctx.setTransform(dpr, 0, 0, dpr, 0, 0);

      dots = [];
      const spacing = Math.max(10, Math.min(width / 40, 17));
      const cols = Math.ceil(width / spacing);
      const rows = Math.ceil(height / spacing);

      for (let r = 0; r <= rows; r++) {
        for (let c = 0; c <= cols; c++) {
          dots.push({
            x: c * spacing,
            y: r * spacing,
            phase: Math.random() * Math.PI * 2,
            row: r,
          });
        }
      }
    };

    const draw = (time: number) => {
      animationId = requestAnimationFrame(draw);

      if (time - lastTime < 40) return;
      lastTime = time;

      ctx.clearRect(0, 0, width, height);
      const t = time * 0.00035;
      const spacing = Math.max(10, Math.min(width / 40, 17));

      const colors = [
        [116, 205, 235], // ice blue
        [174, 159, 225], // soft lavender
        [239, 169, 202], // pale pink
        [160, 177, 191], // silver
      ];

      for (const dot of dots) {
        const x = dot.x;
        const y = dot.y;

        const wave1 = Math.sin(x * 0.009 + t * 2 + dot.phase) * 23;
        const wave2 = Math.sin(x * 0.016 - t * 1.4) * 12;
        const wave3 = Math.cos(y * 0.008 + t) * 5;
        const py = y + wave1 + wave2 + wave3;

        const waveStrength =
          (Math.sin(x * 0.008 + t * 2.5) + 1) / 2;

        const colorPosition =
          (dot.phase / (Math.PI * 2) + t * 0.15) * colors.length;
        const colorIndex =
          Math.floor(colorPosition) % colors.length;
        const nextIndex = (colorIndex + 1) % colors.length;
        const mix = colorPosition - Math.floor(colorPosition);

        const a = colors[colorIndex];
        const b = colors[nextIndex];

        const red = a[0] * (1 - mix) + b[0] * mix;
        const green = a[1] * (1 - mix) + b[1] * mix;
        const blue = a[2] * (1 - mix) + b[2] * mix;

        const alpha = 0.12 + waveStrength * 0.34;
        const radius = 0.65 + waveStrength * 0.45;

        ctx.beginPath();
        ctx.arc(x, py, radius, 0, Math.PI * 2);
        ctx.fillStyle = `rgba(${red},${green},${blue},${alpha})`;
        ctx.fill();
      }

      frame++;
    };

    resize();

    const reducedMotion = window.matchMedia(
      "(prefers-reduced-motion: reduce)"
    );

    let paused = reducedMotion.matches;

    const start = () => {
      if (!paused && !animationId) {
        animationId = requestAnimationFrame(draw);
      }
    };

    const stop = () => {
      cancelAnimationFrame(animationId);
      animationId = 0;
    };

    const handleMotion = () => {
      paused = reducedMotion.matches;
      if (paused) stop();
      else start();
    };

    window.addEventListener("resize", resize);
    reducedMotion.addEventListener("change", handleMotion);
    start();

    return () => {
      stop();
      window.removeEventListener("resize", resize);
      reducedMotion.removeEventListener("change", handleMotion);
    };
  }, []);

  return <canvas ref={canvasRef} className="nexus-wave-canvas" aria-hidden="true" />;
}
