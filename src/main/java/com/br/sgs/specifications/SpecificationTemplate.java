package com.br.sgs.specifications;

import java.util.UUID;

import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.br.sgs.models.AttendenceHistModel;
import com.br.sgs.models.AttendenceModel;
import com.br.sgs.models.ClientModel;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.ProfileModel;
import com.br.sgs.models.QueueHistModel;
import com.br.sgs.models.QueueModel;
import com.br.sgs.models.RoleModel;
import com.br.sgs.models.TerminalModel;
import com.br.sgs.models.UserModel;

import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

public class SpecificationTemplate {

    @And({
            @Spec(path = "document", spec = Like.class),
            @Spec(path = "name", spec = Like.class)
    })
    public interface CompanySpec extends Specification<CompanyModel> {}
    
    @And({
        @Spec(path = "name", spec = Like.class),
        @Spec(path = "status", spec = Equal.class)
    })
	public interface TerminalSpec extends Specification<TerminalModel> {}
    
    @And({
        @Spec(path = "description", spec = Like.class),
        @Spec(path = "status", spec = Equal.class)
    })
	public interface ProfileSpec extends Specification<ProfileModel> {}
    
    @And({
        @Spec(path = "dtCreated", spec = GreaterThanOrEqual.class),
        @Spec(path = "status", spec = Equal.class),
        @Spec(path = "dtUpdated", spec = LessThanOrEqual.class)
    })
	public interface AttendenceSpec extends Specification<AttendenceModel> {}
    
    @And({
        @Spec(path = "dtCreated", spec = GreaterThanOrEqual.class),
        @Spec(path = "status", spec = Equal.class),
        @Spec(path = "queueId", spec = Equal.class),
        @Spec(path = "attendenceId", spec = Equal.class),
        @Spec(path = "dtUpdated", spec = LessThanOrEqual.class)
    })
	public interface AttendenceHistSpec extends Specification<AttendenceHistModel> {}
    
    
    @And({
        @Spec(path = "document", spec = Like.class),
        @Spec(path = "name", spec = Like.class),
        @Spec(path = "organization", spec = Like.class),
	})
	public interface ClientSpec extends Specification<ClientModel> {} 
    
    @And({
        @Spec(path = "description", spec = Like.class)
    })
	public interface RoleSpec extends Specification<RoleModel> {}
    
   
    @And({
        @Spec(path = "status", spec = Equal.class),
        @Spec(path = "description", spec = Like.class)
    })
	public interface QueueSpec extends Specification<QueueModel> {}
    
    @And({
        @Spec(path = "queueHistId", spec = Equal.class),
        @Spec(path = "description", spec = Like.class)
    })
	public interface QueueHistSpec extends Specification<QueueHistModel> {}

    
    @And({
        @Spec(path = "document", spec = Equal.class),
        @Spec(path = "username", spec = Like.class),
        @Spec(path = "email", spec = Like.class),
        @Spec(path = "fullName", spec = Like.class),
	})
	public interface UserSpec extends Specification<UserModel> {}
    
    
    public static Specification<TerminalModel> terminalCompanyId(final UUID companyId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<CompanyModel> company = query.from(CompanyModel.class);
            return cb.and(cb.equal(company.get("companyId"), companyId));
        };
    }
    
    public static Specification<AttendenceModel> attendenceCompanyId(final UUID companyId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<CompanyModel> company = query.from(CompanyModel.class);
            return cb.and(cb.equal(company.get("companyId"), companyId));
        };
    }
    
    public static Specification<AttendenceModel> attendenceCompanyId(final UUID companyId, final String queueId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<CompanyModel> company = query.from(CompanyModel.class);
            Root<QueueModel> queue = query.from(QueueModel.class);
            //;
            
            return cb.and(cb.equal(company.get("companyId"), companyId), cb.equal(queue.get("queueId"), queueId));
        };
    }
    
    public static Specification<AttendenceModel> attendenceCompanyIdQueueId(final UUID companyId, final UUID queueId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<AttendenceModel> attendence = root;
            Root<CompanyModel> company = query.from(CompanyModel.class);
            return cb.and(cb.equal(company.get("companyId"), companyId), cb.equal(attendence.get("queue").get("queueId"), queueId));
        };
    }
    
    
    /*
     * public static Specification<QueueHistModel> queueHistCompanyId(final UUID companyId, final UUID queueId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<QueueHistModel> queueHistModel = root;
            Root<QueueModel> queue = query.from(QueueModel.class);            
            return cb.and(cb.equal(queue.get("company").get("companyId"), companyId), cb.equal(queueHistModel.get("queue").get("queueId"), queueId));
        };
    }
     * 
     * 
    public static Specification<LessonModel> lessonModuleId(final UUID moduleId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<LessonModel> lesson = root;
            Root<ModuleModel> module = query.from(ModuleModel.class);
            Expression<Collection<LessonModel>> moduleLessons = module.get("lessons");
            return cb.and(cb.equal(module.get("moduleId"), moduleId), cb.isMember(lesson, moduleLessons));
        };
    }*/
    
    public static Specification<AttendenceHistModel> attendenceHistCompanyId(final UUID companyId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<CompanyModel> company = query.from(CompanyModel.class);
            return cb.and(cb.equal(company.get("companyId"), companyId));
        };
    }
    
    public static Specification<ProfileModel> profileCompanyId(final UUID companyId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<CompanyModel> company = query.from(CompanyModel.class);
            return cb.and(cb.equal(company.get("companyId"), companyId));
        };
    }
    
    
    public static Specification<ClientModel> clientCompanyId(final UUID companyId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<CompanyModel> company = query.from(CompanyModel.class);
            return cb.and(cb.equal(company.get("companyId"), companyId));
        };
    }
    
    public static Specification<RoleModel> roleCompanyId(final UUID companyId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<CompanyModel> company = query.from(CompanyModel.class);
            return cb.and(cb.equal(company.get("companyId"), companyId));
        };
    }
    
    public static Specification<QueueModel> queueCompanyId(final UUID companyId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<CompanyModel> company = query.from(CompanyModel.class);
            return cb.and(cb.equal(company.get("companyId"), companyId));
        };
    }
    
    
    public static Specification<QueueHistModel> queueHistCompanyId(final UUID companyId, final UUID queueId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<QueueHistModel> queueHistModel = root;
            Root<QueueModel> queue = query.from(QueueModel.class);            
            return cb.and(cb.equal(queue.get("company").get("companyId"), companyId), cb.equal(queueHistModel.get("queue").get("queueId"), queueId));
        };
    }
    
    public static Specification<UserModel> userCompanyId(final UUID companyId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<CompanyModel> company = query.from(CompanyModel.class);
            return cb.and(cb.equal(company.get("companyId"), companyId));
        };
    }
    
    /*
    @And({
            @Spec(path="email", spec= Like.class),
            @Spec(path = "fullName", spec = Like.class),
            @Spec(path="userStatus", spec= Equal.class),
            @Spec(path="userType", spec= Equal.class)})
    public interface UserSpec extends Specification<UserModel> {}

    @Spec(path = "title", spec = Like.class)
    public interface ModuleSpec extends Specification<ModuleModel> {}

    @Spec(path = "title", spec = Like.class)
    public interface LessonSpec extends Specification<LessonModel> {}

    

    public static Specification<LessonModel> lessonModuleId(final UUID moduleId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<LessonModel> lesson = root;
            Root<ModuleModel> module = query.from(ModuleModel.class);
            Expression<Collection<LessonModel>> moduleLessons = module.get("lessons");
            return cb.and(cb.equal(module.get("moduleId"), moduleId), cb.isMember(lesson, moduleLessons));
        };
    }

    public static Specification<UserModel> userCourseId(final UUID courseId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<UserModel> user = root;
            Root<CourseModel> course = query.from(CourseModel.class);
            Expression<Collection<UserModel>> coursesUsers = course.get("users");
            return cb.and(cb.equal(course.get("courseId"), courseId), cb.isMember(user, coursesUsers));
        };
    }

    public static Specification<CourseModel> courseUserId(final UUID userId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<CourseModel> course = root;
            Root<UserModel> user = query.from(UserModel.class);
            Expression<Collection<CourseModel>> usersCourses = user.get("courses");
            return cb.and(cb.equal(user.get("userId"), userId), cb.isMember(course, usersCourses));
        };
    }
    
    */


}
