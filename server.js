const express = require("express");
const cors = require("cors");
require("dotenv").config();
const seatRoutes = require("./routes/seats");

const app = express();
app.use(cors());
app.use(express.json());

app.use("/api/seats", seatRoutes);

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
