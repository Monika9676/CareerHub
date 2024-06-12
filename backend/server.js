const express = require('express');
const bodyParser = require('body-parser');
const sgMail = require('@sendgrid/mail');
const crypto = require('crypto');

const app = express();
const port = 3000;

sgMail.setApiKey('YOUR_SENDGRID_API_KEY');

app.use(bodyParser.json());

const otps = new Map();

app.post('/send-otp', (req, res) => {
    const { email } = req.body;
    const otp = crypto.randomInt(100000, 999999).toString();

    otps.set(email, otp);

    const msg = {
        to: email,
        from: 'monikagirdhar9676@gmail.com',
        subject: 'Your OTP Code',
        text: `Your OTP code is ${otp}. It will expire in 10 minutes.`,
    };

    sgMail.send(msg)
        .then(() => {
            res.json({ success: true });
            setTimeout(() => otps.delete(email), 10 * 60 * 1000);
        })
        .catch(error => {
            console.error(error);
            res.json({ success: false, error: error.toString() });
        });
});

// Endpoint to validate OTP
app.post('/validate-otp', (req, res) => {
    const { email, otp } = req.body;
    const storedOtp = otps.get(email);

    if (storedOtp && storedOtp === otp) {
        otps.delete(email); // OTP validated, remove it
        res.json({ success: true });
    } else {
        res.json({ success: false, message: 'Invalid OTP' });
    }
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}/`);
});
