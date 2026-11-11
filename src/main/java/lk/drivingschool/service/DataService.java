package lk.drivingschool.service;


    public static List<Feedback> getFeedbacks() {
        List<Feedback> list = new ArrayList<>();
        for (String[] r : FileHandler.load("feedback.txt")) {
            if (r.length >= 5) {
                int rating = 5;
                try { rating = Integer.parseInt(r[2]); } catch (Exception ignored) {}
                list.add(new Feedback(r[0], r[1], rating, r[3], r[4]));
            }
        }
        return list;
    }


    public static double getAverageRating() {
        List<Feedback> list = getFeedbacks();
        if (list.isEmpty()) return 5.0;
        double sum = 0.0;
        for (Feedback f : list) {
            sum += f.getRating();
        }
        return sum / list.size();
    }


    public static void addFeedback(Feedback f) {
        String[] row = new String[]{f.getId(), f.getStudentId(), String.valueOf(f.getRating()), f.getComments(), f.getStatus()};
        FileHandler.append("feedback.txt", row);
    }


    public static void updateFeedback(Feedback f) {
        String[] row = new String[]{f.getId(), f.getStudentId(), String.valueOf(f.getRating()), f.getComments(), f.getStatus()};
        FileHandler.update("feedback.txt", f.getId(), row);
    }


}
