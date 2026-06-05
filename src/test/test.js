// test.js
import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 50 }, // ramp-up
        { duration: '1m', target: 50 },  // нагрузка
        { duration: '30s', target: 0 },  // ramp-down
    ],
};

const BASE_URL = 'http://192.168.1.6:8080';

export default function () {

    // =========================================
    // GET /
    // =========================================
    const rootRes = http.get(`${BASE_URL}`);

    check(rootRes, {
        'GET / status 200': (r) => r.status === 200,
    });

    sleep(1);

    // =========================================
    // GET /models/fetch
    // =========================================
    const modelsRes = http.get(`${BASE_URL}/models/fetch`);

    check(modelsRes, {
        'GET /models/fetch status 200': (r) => r.status === 200,
    });

    sleep(1);

    // =========================================
    // POST /categories/fetch
    // BODY REQUIRED
    // =========================================
    const categoriesPayload = JSON.stringify({
        model_id: '72',
    });

    const categoriesRes = http.post(
        `${BASE_URL}/categories/fetch`,
        categoriesPayload,
        {
            headers: {
                'Content-Type': 'application/json',
            },
        }
    );

    check(categoriesRes, {
        'POST /categories/fetch status 200': (r) => r.status === 200,
    });

    sleep(1);

    // =========================================
    // PATCH /models/update
    // BODY REQUIRED
    // =========================================
    const updatePayload = JSON.stringify({
        id: 72,
        name: 'Тест',
        description:
            'Hi everyone! this is my very first model on sketchfab! 3d realistic model of stylish modern sofa by Eichholtz I have used UDIM UV mapping for this one.',
        categories: ['Тест', 'Тест2'],
    });

    const updateRes = http.patch(
        `${BASE_URL}/models/update`,
        updatePayload,
        {
            headers: {
                'Content-Type': 'application/json',
            },
        }
    );

    check(updateRes, {
        'PATCH /models/update status 200': (r) => r.status === 200,
    });

    sleep(1);
}