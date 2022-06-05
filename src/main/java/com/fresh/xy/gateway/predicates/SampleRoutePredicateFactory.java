package com.fresh.xy.gateway.predicates;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.core.style.ToStringCreator;
import org.springframework.web.server.ServerWebExchange;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class SampleRoutePredicateFactory extends AbstractRoutePredicateFactory<SampleRoutePredicateFactory.Config> {

    public SampleRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("patterns", "tag");
    }

    @Override
    public ShortcutType shortcutType() {
        /*return new ShortcutType {
            @Override
            public Map<String, Object> normalize(Map<String, String> args,
                    ShortcutConfigurable shortcutConf, SpelExpressionParser parser,
                    BeanFactory beanFactory) {

            }
        };*/
        return ShortcutType.DEFAULT; //TODO 这也太拉了，这是个enum只有预定义的值，无法实现自定义参数解析法
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        log.info("patterns: {}", config.getPatterns());
        log.info("tags: {}", config.getTags());

        return exchange -> {
            log.info("Sample matches ...");
            return true;
        };
    }

    public static class Config {
        private List<String> patterns;
        private List<String> tags;

        public List<String> getPatterns() {
            return patterns;
        }
        public void setPatterns(List<String> patterns) {
            this.patterns = patterns;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        @Override
        public String toString() {
            return new ToStringCreator(this).append("patterns", patterns).toString();
        }
    }

}
