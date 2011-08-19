Welcome to the Dia Programming Language
=======================================

Another programming language??? Yes! I had a wise friend who said, in essence, 
if you could describe a problem in a language dedicated to the problem domain
it would be simple to understand and to solve.

I love writing languages and have written my share (Tuil, Browz Script, DbSP,
and GSP to name a few). I love learning and using languages. I like many things
from each of them. But none have everything I want. (FYI: my current favorites
are Ruby, Scala, and Java, each for different reasons.) So I'm writing yet one
more language. Will Dia be **The One Language to Rule Them All**? Probably not.
But will this language be way cool and a blast to use? *Absolutely!*

Status
------
We're early in the design stage. We are actively looking for willing, talented
soles to help us get the language to the initial release stage: 0.1 and beyond.

Language Goals
--------------
1. Simple
2. Extensible
3. Leveraging

Simplicity
----------
Dia should be simple to learn, simple to read, and simple to use.
This means it should do things in expected ways. It needs to use well known
idioms from other languages, but not just programming languages. It should
incorporate and support data languages like XML/HTML, JSON, and YAML. It should
support the de-facto database language: SQL. It should be simple to define your
own language extensions. Which brings us to extensibility.

Extensibility
-------------
Many languages claim to be extensible and are to a limited extent. Lisp has 
lambda. C/C++ has #define macros. Scala has automatic infix notation and type
casting. Ruby has method_missing and instance_eval. All of these are used to
create DSLs (domain specific languages). The closest I've encountered is Mirah's
macros. But they are all too limited.

Dia is designed from the foundation to support and rely on language plug-ins.
Plug-ins actually add lexers to the tokenizer, grammar productions to the LR
parser, can modify the AST tree after parsing, and add support to the back-end
code producers. The core language is all built from Plug-ins.

You are encouraged to extend the language yourself. Does this mean you have to
contribute or somehow compile them in to the Dia compiler in order to use them?
No. Simply import a class which extends org.dia.Plugin and you will have access
to all of the language extensions that plugin implements. On the other hand we
actively encourage everyone to send us your awesome language extensions. The
best will eventuall make their way into the language core. Others will be made
available to everyone in the extensions folder.

Leverage
--------
The best modern languages come with extensive, well written and rigorously
tested libraries. There is no way this humble team is going to accomplish
something remotely close in our spare time. So we have to leverage what the
libraries out there. We also need a state of the art virtual machine, for code
reliability, speed and machine and operating system portability.

We have chosen to leverage the Java Platform and the Java Virtual Machine,
as many others wisely have before us. Will there be a version for .NET?
Probably, as soon as someone volunteers to join the team and port it. Is that
someone you?

### Why the name Dia?

When I first thought of it I also thought of a "diamond in the rough". I know
it's going to take years to polish it and get the best language possible. I also
wanted a three letter (or less) extension. Dia can also stand for 'dialect', as
in it supports multiple dialects by having language plug-ins at its foundation
and being easy to extend in any way you want.
