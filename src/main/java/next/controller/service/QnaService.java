//package next.controller.service;
//
//import next.dao.AnswerDao;
//import next.dao.QuestionDao;
//
//public class QnaService {
//    private static QnaService qnaService;
//
//    private QuestionDao questionDao = QuestionDao.getInstance();
//    private AnswerDao answerDao = AnswerDao.getInstance();
//
//    private QnaService() {
//    }
//
//    public static QnaService getInstance() {
//        if (qnaService == null) {
//            qnaService = new QnaService();
//        }
//        return qnaService;
//    }
//
//    public Question findById(long questionId) {
//        return questionDao.findById(questionId);
//    }
//
//}
