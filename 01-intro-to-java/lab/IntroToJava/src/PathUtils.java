public class PathUtils {

    public static void main(String[] args) {
        System.out.println(getCanonicalPath("/home/"));
        System.out.println(getCanonicalPath("/../"));
        System.out.println(getCanonicalPath("/home//foo/"));
        System.out.println(getCanonicalPath("//"));
        System.out.println(getCanonicalPath("/a/./b/../../c/"));
    }

    public static String getCanonicalPath(String path) {
        String[] parts = path.split("/");
        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            if (part.equals("")) {
                continue;
            }
            if (part.equals(".")) {
                continue;
            }
            if (part.equals("..")) {
                int lastIndex = result.lastIndexOf("/");
                if (lastIndex != -1) {
                    result.delete(lastIndex, result.length());
                }
                continue;
            }

            result.append('/');
            result.append(part);
        }

        int length = result.length();
        if (length == 0) {
            result.append('/');
        }

        return result.toString();
    }
}
