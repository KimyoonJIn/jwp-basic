package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static next.controller.UserSessionUtils.getUserFromSession;

public class DeleteQuestionController  extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(DeleteQuestionController.class);

    private AnswerDao answerDao = AnswerDao.getInstance();
    private QuestionDao questionDao = QuestionDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse response) throws Exception {
        // 로그인한 사용자만
        if (getUserFromSession(req.getSession()) == null) {
            return jspView("redirect:/users/loginForm");
        }

        //삭제하고자 하는 유저와 질문자가 같은 사람인지 체크
        Long questionId = Long.parseLong(req.getParameter("questionId"));

        Question question = questionDao.findById(questionId);
        String writer = question.getWriter();

        if (writer != (UserSessionUtils.getUserFromSession(req.getSession())).getUserId()) {
            throw new IllegalStateException("user and writer do not match");
        }

        // 질문에 대한 답변을 뽑고 체
        List<Answer> answers = answerDao.findAllByQuestionId(questionId);
        // 답변이 없는 경우 -> 게시물삭제
        if (answers.isEmpty()) {
            questionDao.delete(questionId);
            return jspView("redirect:/");
        }

        for (Answer answer : answers) {
            // 질문과 답변 작성자가 일치 하지 않을 경
            if (!writer.equals(answer.getWriter())) {
                throw new Exception("Can not delete question");
            }
        }

        questionDao.delete(questionId);
        return jspView("redirect:/");
    }
}
