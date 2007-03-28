/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.jbehave.core.story.codegen.sablecc.node;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.jbehave.core.story.codegen.sablecc.analysis.Analysis;

public final class AScenario extends PScenario
{
    private PScenarioTitle _scenarioTitle_;
    private final LinkedList _context_ = new TypedLinkedList(new Context_Cast());
    private PEvent _event_;
    private final LinkedList _outcome_ = new TypedLinkedList(new Outcome_Cast());

    public AScenario()
    {
    }

    public AScenario(
        PScenarioTitle _scenarioTitle_,
        List _context_,
        PEvent _event_,
        List _outcome_)
    {
        setScenarioTitle(_scenarioTitle_);

        {
            this._context_.clear();
            this._context_.addAll(_context_);
        }

        setEvent(_event_);

        {
            this._outcome_.clear();
            this._outcome_.addAll(_outcome_);
        }

    }
    public Object clone()
    {
        return new AScenario(
            (PScenarioTitle) cloneNode(_scenarioTitle_),
            cloneList(_context_),
            (PEvent) cloneNode(_event_),
            cloneList(_outcome_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAScenario(this);
    }

    public PScenarioTitle getScenarioTitle()
    {
        return _scenarioTitle_;
    }

    public void setScenarioTitle(PScenarioTitle node)
    {
        if(_scenarioTitle_ != null)
        {
            _scenarioTitle_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _scenarioTitle_ = node;
    }

    public LinkedList getContext()
    {
        return _context_;
    }

    public void setContext(List list)
    {
        _context_.clear();
        _context_.addAll(list);
    }

    public PEvent getEvent()
    {
        return _event_;
    }

    public void setEvent(PEvent node)
    {
        if(_event_ != null)
        {
            _event_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _event_ = node;
    }

    public LinkedList getOutcome()
    {
        return _outcome_;
    }

    public void setOutcome(List list)
    {
        _outcome_.clear();
        _outcome_.addAll(list);
    }

    public String toString()
    {
        return ""
            + toString(_scenarioTitle_)
            + toString(_context_)
            + toString(_event_)
            + toString(_outcome_);
    }

    void removeChild(Node child)
    {
        if(_scenarioTitle_ == child)
        {
            _scenarioTitle_ = null;
            return;
        }

        if(_context_.remove(child))
        {
            return;
        }

        if(_event_ == child)
        {
            _event_ = null;
            return;
        }

        if(_outcome_.remove(child))
        {
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_scenarioTitle_ == oldChild)
        {
            setScenarioTitle((PScenarioTitle) newChild);
            return;
        }

        for(ListIterator i = _context_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set(newChild);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        if(_event_ == oldChild)
        {
            setEvent((PEvent) newChild);
            return;
        }

        for(ListIterator i = _outcome_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set(newChild);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

    }

    private class Context_Cast implements Cast
    {
        public Object cast(Object o)
        {
            PContext node = (PContext) o;

            if((node.parent() != null) &&
                (node.parent() != AScenario.this))
            {
                node.parent().removeChild(node);
            }

            if((node.parent() == null) ||
                (node.parent() != AScenario.this))
            {
                node.parent(AScenario.this);
            }

            return node;
        }
    }

    private class Outcome_Cast implements Cast
    {
        public Object cast(Object o)
        {
            POutcome node = (POutcome) o;

            if((node.parent() != null) &&
                (node.parent() != AScenario.this))
            {
                node.parent().removeChild(node);
            }

            if((node.parent() == null) ||
                (node.parent() != AScenario.this))
            {
                node.parent(AScenario.this);
            }

            return node;
        }
    }
}
