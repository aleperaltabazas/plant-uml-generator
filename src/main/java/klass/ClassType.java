package klass;

public enum ClassType {
    Interface {
        public String javaDefinition() {
            return "interface ";
        }
    },
    Abstract {
        public String javaDefinition() {
            return "abstract class ";
        }
    },
    Concrete {
        public String javaDefinition() {
            return "class ";
        }
    };

    public abstract String javaDefinition();
}
