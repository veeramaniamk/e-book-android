package com.saveetha.e_book.request;

public class ApproveBookRequest {
    private int book_id;
    private int publisher_id;

    public ApproveBookRequest(int book_id, int publisher_id) {
        this.book_id = book_id;
        this.publisher_id = publisher_id;
    }

    public static class RejectBookRequest {
        private int book_id;
        private int publisher_id;
        private String cancelMsg;

        public RejectBookRequest(int book_id, int publisher_id, String cancelMsg) {
            this.book_id = book_id;
            this.publisher_id = publisher_id;
            this.cancelMsg = cancelMsg;
        }
    }
}
