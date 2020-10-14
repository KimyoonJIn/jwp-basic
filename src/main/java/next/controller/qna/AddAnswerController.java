package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.QuestionDao;
import next.model.Question;
import next.model.Result;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.AnswerDao;
import next.model.Answer;

import java.util.List;

import static next.controller.UserSessionUtils.getUserFromSession;

public class AddAnswerController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(AddAnswerController.class);

    private AnswerDao answerDao = AnswerDao.getInstance();
    private QuestionDao questionDao = QuestionDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse response) throws Exception {
        // 로그인한 사용자만 코멘트작성이 가능하도록
        if (getUserFromSession(req.getSession()) == null) {
            return jspView("redirect:/users/loginForm");
        }
        User user = getUserFromSession(req.getSession());

        // 코멘트 작성할 때 글쓴이를 입력하지 않고 로그인한 사용자 정보를 가져와 글쓴이 이름으로 사용하도록 구현
        String writer = req.getParameter("writer");
        if (writer == null) {
            writer = user.getUserId();
        }

        Answer answer = new Answer(writer, req.getParameter("contents"),
                Long.parseLong(req.getParameter("questionId")));
        log.debug("answer : {}", answer);

        Answer savedAnswer = answerDao.insert(answer);
        questionDao.addCountOfAnswer(savedAnswer.getQuestionId());

        // TODO : Question AJAX 구현
        Question question = questionDao.findById(savedAnswer.getQuestionId());

        return jsonView().addObject("question", question).addObject("answer", savedAnswer).addObject("result", Result.ok());
    }
}
