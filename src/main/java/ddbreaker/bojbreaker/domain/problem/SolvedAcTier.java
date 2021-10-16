package ddbreaker.bojbreaker.domain.problem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SolvedAcTier {

    UNRATED(0, "Unrated"),

    BRONZE5(1, "Bronze V"),
    BRONZE4(2, "Bronze IV"),
    BRONZE3(3, "Bronze III"),
    BRONZE2(4, "Bronze II"),
    BRONZE1(5, "Bronze I"),

    SILVER5(6, "Silver V"),
    SILVER4(7, "Silver IV"),
    SILVER3(8, "Silver III"),
    SILVER2(9, "Silver II"),
    SILVER1(10, "Silver I"),

    GOLD5(11, "Gold V"),
    GOLD4(12, "Gold IV"),
    GOLD3(13, "Gold III"),
    GOLD2(14, "Gold II"),
    GOLD1(15, "Gold I"),

    PLATINUM5(16, "Platinum V"),
    PLATINUM4(17, "Platinum IV"),
    PLATINUM3(18, "Platinum III"),
    PLATINUM2(19, "Platinum II"),
    PLATINUM1(20, "Platinum I"),

    DIAMOND5(21, "Diamond V"),
    DIAMOND4(22, "Diamond IV"),
    DIAMOND3(23, "Diamond III"),
    DIAMOND2(24, "Diamond II"),
    DIAMOND1(25, "Diamond I"),

    RUBY5(26, "Ruby V"),
    RUBY4(27, "Ruby IV"),
    RUBY3(28, "Ruby III"),
    RUBY2(29, "Ruby II"),
    RUBY1(30, "Ruby I");

    private final int key;
    private final String title;
}
