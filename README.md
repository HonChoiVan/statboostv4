# Stat Boost (Forge 1.20.1)

Bảng chỉ số nâng **Sức mạnh, Máu, Tốc độ, Sức mạnh phép thuật**. Mỗi lần cộng tốn cấp kinh nghiệm.

- Phím mở bảng: **K** (đổi trong Controls)
- 1 điểm = +4%, tối đa 50 điểm/chỉ số = +200%
- Hỗ trợ Iron's Spellbooks (`irons_spellbooks:spell_power`) và To Magic and Extras

## Build bằng GitHub (miễn phí)
1. Tạo repo mới trên GitHub, upload TOÀN BỘ nội dung của zip này (giữ thư mục `.github`).
2. Vào tab **Actions** → workflow **Build mod** tự chạy (hoặc bấm *Run workflow*).
3. Khi xong, mở lần chạy đó → mục **Artifacts** → tải `statboost-mod` → bên trong là file `.jar`.
4. Bỏ file jar vào thư mục `mods` (cả client và server).

## Cấu hình
File `config/statboost-common.toml` (sinh ra sau lần chạy đầu):
- `maxPoints`, `percentPerPoint`, `maxBonusPercent`
- `baseLevelCost`, `levelCostGrowth` (cost = base + floor(điểm đã mua * growth))
- `spellPowerAttributes`: danh sách ID attribute mà "Sức mạnh phép thuật" sẽ tăng.
  Thêm ID attribute spell power của To Magic and Extras vào đây nếu mod đó dùng attribute riêng.

## Lệnh
`/statboost reset <người chơi>` (OP) – đặt lại điểm và gỡ buff. Nên chạy trước khi gỡ mod.
