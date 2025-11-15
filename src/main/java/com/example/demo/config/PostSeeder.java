// package com.example.demo.config; (Hoặc com.example.demo.seeder)
package com.example.demo.config;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;

/**
 * Tự động chèn dữ liệu mẫu cho Post (chủ đề thời trang) khi ứng dụng khởi động.
 */
@Component
public class PostSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PostSeeder.class);
    private final PostRepository postRepository;

    public PostSeeder(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Chỉ seed khi database trống
        if (postRepository.count() == 0) {
            seedPosts();
        } else {
            log.info("Posts table is not empty. Skipping seeder.");
        }
    }

    private void seedPosts() {
        log.info("Seeding fashion posts...");

        // --- Post 1: Xu hướng Xuân Hè ---
        Post post1 = new Post();
        post1.setTitle("Xu hướng thời trang Xuân-Hè 2025: Sắc màu 'Peach Fuzz' lên ngôi");
        post1.setSlug("xu-huong-thoi-trang-xuan-he-2025");
        post1.setDescription("Màu sắc của năm 'Peach Fuzz' (cam đào) hứa hẹn sẽ thống trị các sàn diễn và đường phố. Hãy cùng tìm hiểu cách phối đồ với gam màu này.");
        post1.setContent(
            "<h2>'Peach Fuzz' - Cảm hứng chủ đạo</h2>\n\n" +
            "<p>Pantone đã công bố 'Peach Fuzz' là màu sắc của năm 2025. Đây là một gam màu cam đào nhẹ nhàng, ấm áp, mang lại cảm giác dễ chịu và tinh tế. Trong mùa Xuân-Hè này, 'Peach Fuzz' xuất hiện trên mọi item, từ những chiếc váy lụa thướt tha đến áo blazer oversized.</p>\n\n" +
            "<h3>Cách phối đồ:</h3>\n" +
            "<ul>\n" +
            "    <li><strong>Monochrome (Đơn sắc):</strong> Thử một bộ suit màu 'Peach Fuzz' cho vẻ ngoài thanh lịch.</li>\n" +
            "    <li><strong>Kết hợp với màu trung tính:</strong> Phối cùng màu trắng, beige hoặc xám để tạo sự cân bằng.</li>\n" +
            "    <li><strong>Phụ kiện:</strong> Một chiếc túi xách hoặc đôi giày màu cam đào sẽ là điểm nhấn hoàn hảo.</li>\n" +
            "</ul>"
        );
        post1.setImage("fashion-peach-fuzz-2025.jpg");
        post1.setStatus(true);

        // --- Post 2: Tủ đồ con nhộng ---
        Post post2 = new Post();
        post2.setTitle("Xây dựng tủ đồ con nhộng (Capsule Wardrobe): 10 item cơ bản phải có");
        post2.setSlug("xay-dung-tu-do-con-nhong-10-item");
        post2.setDescription("Một tủ đồ con nhộng thông minh giúp bạn tiết kiệm tiền bạc, thời gian và luôn mặc đẹp chỉ với vài item cơ bản.");
        post2.setContent(
            "<p><strong>Capsule Wardrobe</strong> là một bộ sưu tập các item quần áo cơ bản, linh hoạt, có thể dễ dàng phối hợp với nhau. Mục tiêu là tạo ra nhiều outfit nhất chỉ với số lượng quần áo ít nhất.</p>\n\n" +
            "<h3>10 item cơ bản:</h3>\n" +
            "<ol>\n" +
            "    <li>Áo sơ mi trắng.</li>\n" +
            "    <li>Áo phông trơn (trắng/đen/xám).</li>\n" +
            "    <li>Quần jeans ống đứng vừa vặn.</li>\n" +
            "    <li>Một chiếc váy đen (Little Black Dress).</li>\n" +
            "    <li>Áo blazer/jacket.</li>\n" +
            "    <li>Áo len mỏng (cho mùa lạnh).</li>\n" +
            "    <li>Quần âu ống suông.</li>\n" +
            "    <li>Giày sneaker trắng.</li>\n" +
            "    <li>Một đôi giày cao gót/giày bệt lịch sự.</li>\n" +
            "    <li>Một chiếc túi xách đa dụng.</li>\n" +
            "</ol>"
        );
        post2.setImage("fashion-capsule-wardrobe.jpg");
        post2.setStatus(true);

        // --- Post 3: Thời trang bền vững ---
        Post post3 = new Post();
        post3.setTitle("Thời trang bền vững: Không chỉ là một xu hướng");
        post3.setSlug("thoi-trang-ben-vung-la-gi");
        post3.setDescription("Ngành công nghiệp thời trang đang thay đổi. Tìm hiểu về thời trang bền vững và tại sao nó lại quan trọng cho tương lai của chúng ta.");
        post3.setContent(
            "<p>Thời trang bền vững (Sustainable Fashion) là một khái niệm bao trùm việc sản xuất, tiêu thụ và thải bỏ quần áo một cách có trách nhiệm với môi trường và xã hội.</p>\n\n" +
            "<p>Nó không chỉ là về việc sử dụng vật liệu thân thiện với môi trường (như cotton organic, vải tái chế), mà còn là về việc đảm bảo điều kiện làm việc công bằng cho công nhân may mặc, giảm thiểu rác thải (thời trang nhanh) và khuyến khích người tiêu dùng mua ít hơn nhưng chất lượng hơn.</p>"
        );
        post3.setImage("fashion-sustainable.jpg");
        post3.setStatus(true);
        
        // --- Post 4: Phong cách cá nhân ---
        Post post4 = new Post();
        post4.setTitle("Làm thế nào để tìm thấy phong cách thời trang cá nhân của bạn?");
        post4.setSlug("tim-phong-cach-ca-nhan");
        post4.setDescription("Đừng chạy theo xu hướng. Hãy tạo ra phong cách của riêng bạn. Đây là 5 bước giúp bạn định hình gu thẩm mỹ cá nhân.");
        post4.setContent(
            "<h3>1. Tìm nguồn cảm hứng</h3>\n" +
            "<p>Thu thập hình ảnh từ Pinterest, Instagram, tạp chí. Bạn bị thu hút bởi phong cách nào? (Tối giản, cổ điển, năng động, lãng mạn...).</p>\n" +
            "<h3>2. Kiểm tra tủ đồ</h3>\n" +
            "<p>Những món đồ nào bạn mặc thường xuyên nhất? Tại sao bạn thích chúng? (Thoải mái, màu sắc, kiểu dáng...).</p>\n" +
            "<h3>3. Tạo 'Mood Board'</h3>\n" +
            "<p>Tổng hợp các hình ảnh bạn thích để thấy rõ bức tranh tổng thể về phong cách của bạn.</p>\n" +
            "<h3>4. Thử nghiệm</h3>\n" +
            "<p>Đừng ngại thử những thứ mới. Hãy thử phối đồ theo cách khác biệt.</p>\n" +
            "<h3>5. Tin vào bản thân</h3>\n" +
            "<p>Điều quan trọng nhất là bạn cảm thấy thoải mái và tự tin trong bộ đồ mình mặc.</p>"
        );
        post4.setImage("fashion-personal-style.jpg");
        post4.setStatus(true);

        // --- Post 5: Local Brands ---
        Post post5 = new Post();
        post5.setTitle("5 thương hiệu thời trang Việt (Local Brand) đang 'làm mưa làm gió'");
        post5.setSlug("top-5-local-brand-viet-nam");
        post5.setDescription("Thời trang Việt Nam đang phát triển mạnh mẽ với nhiều thương hiệu nội địa chất lượng, thiết kế độc đáo và giá cả hợp lý.");
        post5.setContent(
            "<p>Sự trỗi dậy của các 'local brand' đã mang đến một làn gió mới cho thị trường. Thay vì chỉ chạy theo các ông lớn quốc tế, giới trẻ Việt Nam ngày càng tự hào khi diện trên mình những sản phẩm được thiết kế bởi chính người Việt.</p>\n" +
            "<p>1. [Tên Brand 1]: Nổi tiếng với phong cách đường phố (streetwear).</p>\n" +
            "<p>2. [Tên Brand 2]: Thanh lịch, tối giản (minimalism).</p>\n" +
            "<p>3. [Tên Brand 3]: Chuyên về áo dài cách tân.</p>\n" +
            "<p>4. [Tên Brand 4]:...</p>\n" +
            "<p>5. [Tên Brand 5]:...</p>"
        );
        post5.setImage("fashion-local-brands.jpg");
        post5.setStatus(true);

        // --- Post 6: Xu hướng Thu Đông ---
        Post post6 = new Post();
        post6.setTitle("Dự đoán xu hướng Thu-Đông 2025: Sự trở lại của chất liệu da và len");
        post6.setSlug("xu-huong-thu-dong-2025");
        post6.setDescription("Khi không khí bắt đầu se lạnh, đã đến lúc cập nhật tủ đồ với những chất liệu ấm áp. Da, len xù và vải tweed sẽ là 'key items' của mùa này.");
        post6.setContent(
            "<p>Mùa mốt Thu-Đông 2025 chào đón sự trở lại của những chất liệu cổ điển nhưng được làm mới. Áo khoác da (leather trench coat), áo len vặn thừng (cable-knit) và chân váy vải tweed (tua rua) sẽ là những món đồ bạn nên đầu tư.</p>"
        );
        post6.setImage("fashion-autumn-winter-2025.jpg");
        post6.setStatus(true);

        // --- Post 7: Phối màu ---
        Post post7 = new Post();
        post7.setTitle("Bánh xe màu sắc: 'Chìa khóa vàng' để phối đồ thời trang");
        post7.setSlug("bi-quyet-phoi-mau-quan-ao");
        post7.setDescription("Hiểu về bánh xe màu sắc sẽ giúp bạn nâng tầm khả năng phối đồ, từ an toàn (màu tương đồng) đến cá tính (màu tương phản).");
        post7.setContent(
            "<p>Có 3 cách phối màu cơ bản:</p>\n" +
            "<ul>\n" +
            "    <li><strong>Phối màu đơn sắc (Monochromatic):</strong> Sử dụng các sắc độ khác nhau của cùng một màu. Luôn an toàn và thanh lịch.</li>\n" +
            "    <li><strong>Phối màu tương đồng (Analogous):</strong> Sử dụng các màu nằm cạnh nhau trên bánh xe màu sắc (ví dụ: cam, vàng, xanh lá mạ).</li>\n" +
            "    <li><strong>Phối màu tương phản (Complementary):</strong> Sử dụng các màu đối diện nhau (ví dụ: xanh dương và cam). Rất nổi bật và cá tính.</li>\n" +
            "</ul>"
        );
        post7.setImage("fashion-color-wheel.jpg");
        post7.setStatus(true);

        // --- Post 8: Lịch sử Áo Dài ---
        Post post8 = new Post();
        post8.setTitle("Lịch sử Áo Dài Việt Nam: Biểu tượng của vẻ đẹp và tinh thần dân tộc");
        post8.setSlug("lich-su-ao-dai-viet-nam");
        post8.setDescription("Không chỉ là một trang phục truyền thống, Áo Dài là một di sản văn hóa, chứa đựng câu chuyện lịch sử hàng trăm năm của Việt Nam.");
        post8.setContent(
            "<p>Từ áo giao lãnh, áo tứ thân, áo ngũ thân cho đến tà áo dài Lemur tân thời (những năm 1930) và áo dài Trần Lệ Xuân (những năm 1950), Áo Dài đã trải qua nhiều biến đổi để có được hình dáng thanh lịch và tôn vinh đường nét cơ thể người phụ nữ như ngày nay.</p>"
        );
        post8.setImage("fashion-ao-dai-history.jpg");
        post8.setStatus(true);
        
        // --- Post 9: Phụ kiện ---
        Post post9 = new Post();
        post9.setTitle("Bí quyết nâng tầm trang phục bằng phụ kiện");
        post9.setSlug("cach-dung-phu-kien-thoi-trang");
        post9.setDescription("Một bộ trang phục đơn giản có thể trở nên đẳng cấp nếu bạn biết cách chọn và phối phụ kiện thông minh.");
        post9.setContent(
            "<p>Phụ kiện là linh hồn của bộ trang phục. Một chiếc áo phông trắng và quần jeans sẽ hoàn toàn khác biệt khi bạn thêm vào:</p>\n" +
            "<ul>\n" +
            "    <li>Một chiếc thắt lưng da bản to.</li>\n" +
            "    <li>Vài lớp dây chuyền (layering necklaces).</li>\n" +
            "    <li>Một chiếc khăn lụa (silk scarf) buộc ở cổ hoặc túi xách.</li>\n" +
            "    <li>Một đôi khuyên tai statement (bản to, nổi bật).</li>\n" +
            "</ul>"
        );
        post9.setImage("fashion-accessories.jpg");
        post9.setStatus(true);
        
        // --- Post 10: Draft (Bài nháp - ẨN) ---
        Post post10 = new Post();
        post10.setTitle("Bài viết nháp: Bộ sưu tập mới (ẨN)");
        post10.setSlug("bo-suu-tap-moi-nhap");
        post10.setDescription("Đây là bài viết nháp, chuẩn bị cho ra mắt bộ sưu tập sắp tới.");
        post10.setContent("<p>Nội dung đang được soạn thảo... (Test bài viết có status = false)</p>");
        post10.setImage("fashion-draft-collection.jpg");
        post10.setStatus(false); // Quan trọng: Đặt là false

        // Lưu tất cả vào database
        List<Post> posts = Arrays.asList(post1, post2, post3, post4, post5, post6, post7, post8, post9, post10);
        postRepository.saveAll(posts);

        log.info("Seeded 10 fashion posts successfully.");
    }
}