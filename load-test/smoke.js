// 부하 테스트 기본 스크립트.
// 실행 예:
//   docker compose --env-file .env -f docker/infra-compose.yml --profile load run --rm k6 \
//     run -e TARGET_PATH=/api/v1/products/1 /scripts/smoke.js
//
// 환경변수
//   BASE_URL     앱 주소 (기본값: http://host.docker.internal:8080)
//   TARGET_PATH  호출할 API 경로 (필수)
//   METHOD       GET 또는 POST (기본값: GET)
//   VUS          동시 사용자 수 (기본값: 50)
//   DURATION     실행 시간 (기본값: 30s)
import http from 'k6/http';
import { check } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://host.docker.internal:8080';
const TARGET_PATH = __ENV.TARGET_PATH;
const METHOD = (__ENV.METHOD || 'GET').toUpperCase();

if (!TARGET_PATH) {
  throw new Error('TARGET_PATH 를 지정해야 합니다. 예: -e TARGET_PATH=/api/v1/products/1');
}

export const options = {
  vus: Number(__ENV.VUS || 50),
  duration: __ENV.DURATION || '30s',
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<500', 'p(99)<1000'],
  },
};

export default function () {
  const url = `${BASE_URL}${TARGET_PATH}`;
  const params = { headers: { 'Content-Type': 'application/json' } };
  const res = METHOD === 'POST' ? http.post(url, '{}', params) : http.get(url, params);
  check(res, { 'status is 2xx': (r) => r.status >= 200 && r.status < 300 });
}
