// Generated code from Butter Knife. Do not modify!
package projet.utilisateur;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ConnexionActivity$$ViewBinder<T extends projet.utilisateur.ConnexionActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558544, "field '_pseudoText'");
    target._pseudoText = finder.castView(view, 2131558544, "field '_pseudoText'");
    view = finder.findRequiredView(source, 2131558545, "field '_passwordText'");
    target._passwordText = finder.castView(view, 2131558545, "field '_passwordText'");
    view = finder.findRequiredView(source, 2131558546, "field '_loginButton'");
    target._loginButton = finder.castView(view, 2131558546, "field '_loginButton'");
    view = finder.findRequiredView(source, 2131558547, "field '_signupLink'");
    target._signupLink = finder.castView(view, 2131558547, "field '_signupLink'");
  }

  @Override public void unbind(T target) {
    target._pseudoText = null;
    target._passwordText = null;
    target._loginButton = null;
    target._signupLink = null;
  }
}
