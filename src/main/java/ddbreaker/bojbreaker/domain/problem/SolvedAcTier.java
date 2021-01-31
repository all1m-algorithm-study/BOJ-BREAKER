package ddbreaker.bojbreaker.domain.problem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SolvedAcTier {

    UNRATED("TIER_UNRATED", "Unrated"),

    BRONZE5("TIER_BRONZE5", "Bronze V"),
    BRONZE4("TIER_BRONZE4", "Bronze IV"),
    BRONZE3("TIER_BRONZE3", "Bronze III"),
    BRONZE2("TIER_BRONZE2", "Bronze II"),
    BRONZE1("TIER_BRONZE1", "Bronze I"),

    SILVER5("TIER_SILVER5", "Silver V"),
    SILVER4("TIER_SILVER4", "Silver IV"),
    SILVER3("TIER_SILVER3", "Silver III"),
    SILVER2("TIER_SILVER2", "Silver II"),
    SILVER1("TIER_SILVER1", "Silver I"),

    GOLD5("TIER_GOLD5", "Gold V"),
    GOLD4("TIER_GOLD4", "Gold IV"),
    GOLD3("TIER_GOLD3", "Gold III"),
    GOLD2("TIER_GOLD2", "Gold II"),
    GOLD1("TIER_GOLD1", "Gold I"),

    PLATINUM5("TIER_PLATINUM5", "Platinum V"),
    PLATINUM4("TIER_PLATINUM4", "Platinum IV"),
    PLATINUM3("TIER_PLATINUM3", "Platinum III"),
    PLATINUM2("TIER_PLATINUM2", "Platinum II"),
    PLATINUM1("TIER_PLATINUM1", "Platinum I"),

    DIAMOND5("TIER_DIAMOND5", "Diamond V"),
    DIAMOND4("TIER_DIAMOND4", "Diamond IV"),
    DIAMOND3("TIER_DIAMOND3", "Diamond III"),
    DIAMOND2("TIER_DIAMOND2", "Diamond II"),
    DIAMOND1("TIER_DIAMOND1", "Diamond I"),

    RUBY5("TIER_RUBY5", "Ruby V"),
    RUBY4("TIER_RUBY4", "Ruby IV"),
    RUBY3("TIER_RUBY3", "Ruby III"),
    RUBY2("TIER_RUBY2", "Ruby II"),
    RUBY1("TIER_RUBY1", "Ruby I");

    private final String key;
    private final String title;
}
