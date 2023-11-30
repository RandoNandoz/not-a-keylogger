# NOT A KEYLOGGER

### What does it do?

Essentially, the application records previous keyboard and mouse inputs ~~and plays them back.~~

### Who uses it?

Sometimes, you just want to automate a repetitive task on your computer that requires you to
repeatedly click on the screen, like for example some UBC courses involve a certain program that
starts with an i and ends in an r ;)

### Why am I interested?

There are some repetitive tasks that occur in not-well-designed programs that do not have keyboard shortcuts
that I would like to automate.

For instance:

- In vanilla minecraft, repetitive clicking is required to empty your inventory into a chest, which makes
  it quite painful to empty an inventory. There aren't any keyboard shortcuts for this as well. While mods
  do fix this issue, not all Minecraft servers support mods.
- That app that starts with an i and ends in r.

<details open>
<summary>User stories</summary>

- I want to record my actions (mouse and keyboard events). ✅
- I want to delete my actions ✅
- I want to be able to play back these mouse and keyboard events. 
- I want to be able to see what keyboard recordings are available to me. ✅
- I want to preview the actions in an "intuitive way" before I play them. ✅
- I want an easy to remember shortcut in order to emergency stop an instruction.
- I want to be able to edit the instructions I have recorded. ✅
- I want to be able to save all my recordings to a file.
- Alternatively, I want to be able to save individual recordings to a file.
- I want to be able to add an individual, or group of recordings from a file when I start the app, if I choose to do so.
- I want to be able to view my recorded actions in a list style, scrollable view. ✅
</details>


[Trello Board for Necromancer](https://trello.com/invite/b/LAWFnGcE/ATTIa9309b5f96d4a29a205ae0e6e418beb49B377540/necromancer-cpsc-210-term-project)

### Instructions for Grader

- You can generate the first required action related to the user story "I want to record my actions (mouse and keyboard events)" by clicking start recording and stopping when needed
- You can generate the second required action related to the user story "I want to delete my actions" by clicking delete
- You can locate the visual component when you start the app and see the beautiful gif
- You can save the state of my app by going to the bar and file > save
- You can load the state of my app by going to the bar and file > load

### Phase 4: Task 3

A refactor I could have made was turn `RecordingController` into a singleton, rather than using dependency injection
however, this comes with tradeoffs. First of all, the singleton design pattern violates the SRP (it manages its own instantiation, and also does other things), and a usage of a singleton
as what is essentially a global variable for the objects in the app is a poor design choice that would increase my coupling.

Secondly, if we were to test the ui package, the singleton would make unit testing much harder, as my listeners are now implicitly
depending on `RecordingController`, and rather than passing a fake `RecordingController` in, I would have to somehow control the state of the
`RecordingController` singleton. In my view, the singleton pattern is quite harmful, and should be used less. This comes from experience
as I used to (and still do, to be honest) use them as a quick hack for my own projects, and when it came time to refactor, every class would somehow be dependent on this
god object.

I could have also re-factored `AppState` to not be a singleton, and instead pass it around. I had intended to refactor my app to eliminate it (see previous rant on why singletons are bad),
however, I got lazy and I had other things to do than work on this project, so I opted not to. However, this refactor might not
improve the design much, because then this would give the `RecordingController` more responsibilities, again violating the SRP.

Lastly, `RecordingController` should really be refactored. Currently it's in charge of both maintaining a collection of input recordings, editing them,
and updating the UI. To improve the cohesion of the system, I would split it up into a `RecordingManager` and `UIManager` class.