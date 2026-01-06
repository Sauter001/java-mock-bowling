import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends NsTest {

    private void runWithoutEnd(String... args) {
        // 게임이 끝나지 않는 입력을 위한 헬퍼 메서드
        // 실제 구현 시 타임아웃 등 고려 필요
    }

    @Override
    protected void runMain() {
        Application.main(new String[]{});
    }

    @Nested
    @DisplayName("플레이어 등록 테스트")
    class PlayerRegistrationTest {

        @Test
        @DisplayName("정상 등록")
        void 정상_등록() {
            assertSimpleTest(() -> {
                run("PJS", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10");
                assertThat(output()).contains("PJS 님의 게임을 시작합니다");
            });
        }

        @Test
        @DisplayName("이름 형식 오류 - 3자 미만")
        void 이름_3자_미만() {
            assertSimpleTest(() -> {
                run("AB", "ABC", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10");
                assertThat(output()).contains("[ERROR] 이름은 영문 3자여야 합니다");
            });
        }

        @Test
        @DisplayName("이름 형식 오류 - 숫자 포함")
        void 이름_숫자_포함() {
            assertSimpleTest(() -> {
                run("A12", "ABC", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10");
                assertThat(output()).contains("[ERROR] 이름은 영문 3자여야 합니다");
            });
        }
    }

    @Nested
    @DisplayName("일반 프레임 테스트")
    class NormalFrameTest {

        @Test
        @DisplayName("스트라이크")
        void 스트라이크() {
            assertSimpleTest(() -> {
                run("PJS", "10", "3", "4", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                assertThat(output()).contains("X");
            });
        }

        @Test
        @DisplayName("스페어")
        void 스페어() {
            assertSimpleTest(() -> {
                run("PJS", "7", "3", "5", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                assertThat(output()).contains("7/");
            });
        }

        @Test
        @DisplayName("미스")
        void 미스() {
            assertSimpleTest(() -> {
                run("PJS", "3", "4", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                assertThat(output()).contains("3 4");
            });
        }

        @Test
        @DisplayName("거터")
        void 거터() {
            assertSimpleTest(() -> {
                run("PJS", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                assertThat(output()).contains("-");
            });
        }
    }

    @Nested
    @DisplayName("10프레임 테스트")
    class FinalFrameTest {

        @Test
        @DisplayName("10프레임 스트라이크 3번")
        void 스트라이크_3번() {
            assertSimpleTest(() -> {
                run("PJS",
                        "10", "10", "10", "10", "10", "10", "10", "10", "10", // 1~9프레임 스트라이크
                        "10", "10", "10"); // 10프레임 스트라이크 3번
                assertThat(output()).contains("X X X");
                assertThat(output()).contains("300"); // 퍼펙트 게임
            });
        }

        @Test
        @DisplayName("10프레임 스페어 후 추가 투구")
        void 스페어_후_추가투구() {
            assertSimpleTest(() -> {
                run("PJS",
                        "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", // 1~9프레임
                        "7", "3", "5"); // 10프레임 스페어 + 추가
                assertThat(output()).contains("7/ 5");
            });
        }

        @Test
        @DisplayName("10프레임 미스 - 2번만 투구")
        void 미스_2번만() {
            assertSimpleTest(() -> {
                run("PJS",
                        "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", // 1~9프레임
                        "3", "5"); // 10프레임 미스
                assertThat(output()).contains("게임이 종료되었습니다");
            });
        }

        @Test
        @DisplayName("10프레임 스트라이크 후 스페어")
        void 스트라이크_후_스페어() {
            assertSimpleTest(() -> {
                run("PJS",
                        "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", // 1~9프레임
                        "10", "7", "3"); // 10프레임 스트라이크 + 스페어
                assertThat(output()).contains("X 7/");
            });
        }
    }

    @Nested
    @DisplayName("점수 계산 테스트")
    class ScoreCalculationTest {

        @Test
        @DisplayName("퍼펙트 게임 - 300점")
        void 퍼펙트_게임() {
            assertSimpleTest(() -> {
                run("PJS",
                        "10", "10", "10", "10", "10", "10", "10", "10", "10",
                        "10", "10", "10");
                assertThat(output()).contains("300");
            });
        }

        @Test
        @DisplayName("거터 게임 - 0점")
        void 거터_게임() {
            assertSimpleTest(() -> {
                run("PJS",
                        "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                        "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                assertThat(output()).contains("0");
            });
        }

        @Test
        @DisplayName("스트라이크 점수 계산")
        void 스트라이크_점수() {
            assertSimpleTest(() -> {
                run("PJS",
                        "10",       // 1프레임: 스트라이크
                        "3", "4",   // 2프레임: 3 + 4 = 7
                        "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                // 1프레임: 10 + 3 + 4 = 17
                assertThat(output()).contains("17");
            });
        }

        @Test
        @DisplayName("스페어 점수 계산")
        void 스페어_점수() {
            assertSimpleTest(() -> {
                run("PJS",
                        "7", "3",   // 1프레임: 스페어
                        "5", "0",   // 2프레임: 5 + 0 = 5
                        "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                // 1프레임: 10 + 5 = 15
                assertThat(output()).contains("15");
            });
        }

        @Test
        @DisplayName("더블 (스트라이크 연속)")
        void 더블() {
            assertSimpleTest(() -> {
                run("PJS",
                        "10",       // 1프레임: 스트라이크
                        "10",       // 2프레임: 스트라이크
                        "5", "3",   // 3프레임: 5 + 3 = 8
                        "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                // 1프레임: 10 + 10 + 5 = 25
                assertThat(output()).contains("25");
            });
        }

        @Test
        @DisplayName("터키 (스트라이크 3연속)")
        void 터키() {
            assertSimpleTest(() -> {
                run("PJS",
                        "10",       // 1프레임: 스트라이크
                        "10",       // 2프레임: 스트라이크
                        "10",       // 3프레임: 스트라이크
                        "0", "0",   // 4프레임: 0
                        "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                // 1프레임: 10 + 10 + 10 = 30
                assertThat(output()).contains("30");
            });
        }
    }

    @Nested
    @DisplayName("입력 예외 테스트")
    class InputExceptionTest {

        @Test
        @DisplayName("핀 수 범위 초과")
        void 핀_수_범위_초과() {
            assertSimpleTest(() -> {
                run("PJS", "11", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10");
                assertThat(output()).contains("[ERROR] 핀 수는 0~10 사이여야 합니다");
            });
        }

        @Test
        @DisplayName("핀 수 음수")
        void 핀_수_음수() {
            assertSimpleTest(() -> {
                run("PJS", "-1", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10");
                assertThat(output()).contains("[ERROR] 핀 수는 0~10 사이여야 합니다");
            });
        }

        @Test
        @DisplayName("남은 핀 초과")
        void 남은_핀_초과() {
            assertSimpleTest(() -> {
                run("PJS", "7", "5", "3", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                assertThat(output()).contains("[ERROR] 남은 핀보다 많은 수를 쓰러뜨릴 수 없습니다");
            });
        }

        @Test
        @DisplayName("숫자가 아닌 입력")
        void 숫자_아님() {
            assertSimpleTest(() -> {
                run("PJS", "abc", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10");
                assertThat(output()).contains("[ERROR] 숫자를 입력해 주세요");
            });
        }
    }

    @Nested
    @DisplayName("점수 보류 테스트")
    class ScorePendingTest {

        @Test
        @DisplayName("스트라이크 후 점수 보류")
        void 스트라이크_점수_보류() {
            assertSimpleTest(() -> {
                runWithoutEnd("PJS", "10");
                // 1프레임 스트라이크 후 점수는 빈칸이어야 함
                assertThat(output()).contains("| X   |");
            });
        }

        @Test
        @DisplayName("스페어 후 점수 보류")
        void 스페어_점수_보류() {
            assertSimpleTest(() -> {
                runWithoutEnd("PJS", "7", "3");
                // 1프레임 스페어 후 점수는 빈칸이어야 함
                assertThat(output()).contains("|  7/ |");
            });
        }
    }
}
