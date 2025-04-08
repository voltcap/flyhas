const express = require("express");
const router = express.Router();
const db = require("../db");

router.get("/booked", (req, res) => {
  db.query("SELECT seat_label FROM booked_seats", (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }
    res.json(results.map((row) => row.seat_label));
  });
});

router.post("/book", (req, res) => {
  const { seats } = req.body; 

  if (!seats || !Array.isArray(seats)) {
    return res.status(400).json({ error: "Invalid seat list" });
  }

  const values = seats.map((seat) => [seat]);

  const sql = "INSERT INTO booked_seats (seat_label) VALUES ?";
  db.query(sql, [values], (err) => {
    if (err) {
      return res.status(500).json({ error: "Booking failed" });
    }
    res.json({ message: "Seats booked successfully!" });
  });
});

module.exports = router;
