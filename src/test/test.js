// test.js - скрипт для k6
import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 50 },  // ramp-up
        { duration: '1m', target: 50 },   // steady
        { duration: '30s', target: 0 },   // ramp-down
    ],
};

export default function () {
    const res = http.get('http://192.168.1.6:8080');
    check(res, { 'status is 200': (r) => r.status === 200 });
    sleep(1);
}